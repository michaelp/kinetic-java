/**
 * Copyright (C) 2014 Seagate Technology.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.seagate.kinetic.client.internal.util.bigobject;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Arrays;

import kinetic.client.ClientConfiguration;
import kinetic.client.KineticException;

/**
 * Internal test to sanity test BigObject util API.
 * 
 * @author chiaming
 * 
 */
public class BigObjectSanityTestRunner implements Runnable {

	private PipedInputStream pis = null;

	private PipedOutputStream pos = null;

	private static final int CHUNK_SIZE = 1024 * 1024 * 10;

	private int nchunk = 1;

	public BigObjectSanityTestRunner(int mBytes) throws IOException {

		if (mBytes < 10) {
			nchunk = 1;
			System.out.println("testing with 10M size ...");
		} else {
			nchunk = mBytes / 10;
		}

		pis = new PipedInputStream(CHUNK_SIZE);
		pos = new PipedOutputStream(pis);
	}

	public InputStream getInputStream() {
		return pis;
	}

	@Override
	public void run() {

		try {

			long total = 0;

			byte[] bytes = new byte[CHUNK_SIZE];

			for (int i = 0; i < nchunk; i++) {
				Arrays.fill(bytes, (byte) 0x00);
				pos.write(bytes);
				pos.flush();

				total = total + CHUNK_SIZE;
			}

			pos.close();

			System.out.println("closed pos, wrote total (bytes) = " + total);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws IOException, KineticException {

		//
		int size = 10;

		if (args.length >= 1) {
			size = Integer.parseInt(args[0]);
		}


		BigObjectSanityTestRunner tester = new BigObjectSanityTestRunner(
				size);

		InputStream is = tester.getInputStream();

		Thread t = new Thread(tester);
		t.start();

		ClientConfiguration config = new ClientConfiguration();

		if (args.length == 2) {
			config.setHost(args[1]);
		}

		BigObject kc = new BigObject(config);

		byte[] key = "bigObject".getBytes();

		long start = System.currentTimeMillis();
		double total = kc.putx(key, is);

		long end = System.currentTimeMillis();

		double time = (end - start) / 1000.0;

		double avg = (total / time) / (1024 * 1024);

		is.close();

		System.out.println("putx time = " + time + ", total = " + total
				+ ", avg (Mb/s)= " + avg);

		//
		NullOutputStream nos = new NullOutputStream();
		start = System.currentTimeMillis();
		long total2 = kc.getx(key, nos);
		end = System.currentTimeMillis();

		if (total2 != total) {
			throw new RuntimeException("getx total != putx total");
		}

		time = (end - start) / 1000.0;

		avg = (total2 / time) / (1024 * 1024);

		System.out.println("getx time = " + time + ", total = " + total2
				+ ", avg (Mb/s)= "
				+ avg);

		start = System.currentTimeMillis();
		long deletedCount = kc.deletex(key);
		end = System.currentTimeMillis();

		time = (end - start) / 1000.0;

		avg = deletedCount / time;

		System.out.println("deletex time = " + time + ", deletedCount = "
				+ deletedCount + ", avg (ops/s) = " + avg);
	}

}
