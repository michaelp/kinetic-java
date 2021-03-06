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
package com.seagate.kinetic.example.batchop;

import java.io.UnsupportedEncodingException;

import kinetic.client.BatchOperation;
import kinetic.client.ClientConfiguration;
import kinetic.client.Entry;
import kinetic.client.KineticClient;
import kinetic.client.KineticClientFactory;
import kinetic.client.KineticException;

/**
 * Kinetic client batch operation usage example.
 * <p>
 * This example shows how to use the batch operation API to abort a batch
 * operation.
 * 
 * @author chiaming
 * @see BatchOperation#abort()
 */
public class BatchOperationAbortExample {

    public void run(String host, int port) throws KineticException,
            UnsupportedEncodingException {

        // kinetic client
        KineticClient client = null;

        // Client configuration and initialization
        ClientConfiguration clientConfig = new ClientConfiguration();

        clientConfig.setHost(host);
        clientConfig.setUseSsl(true);
        clientConfig.setPort(8443);
        // clientConfig.setPort(port);

        // create client instance
        client = KineticClientFactory.createInstance(clientConfig);

        // put entry bar
        Entry bar = new Entry();
        bar.setKey("bar".getBytes("UTF8"));
        bar.setValue("bar".getBytes("UTF8"));
        bar.getEntryMetadata().setVersion("1234".getBytes("UTF8"));

        client.putForced(bar);

        // clean up before batch op
        client.deleteForced("foo".getBytes("UTF8"));

        // start batch a new batch operation
        BatchOperation batch = client.createBatchOperation();

        // put foo
        Entry foo = new Entry();
        foo.setKey("foo".getBytes("UTF8"));
        foo.setValue("foo".getBytes("UTF8"));
        foo.getEntryMetadata().setVersion("5678".getBytes("UTF8"));

        batch.putForced(foo);

        // delete bar
        batch.delete(bar);

        // end/commit batch operation
        batch.abort();

        // start verifying result

        // get foo, expect to find it
        Entry foo1 = client.get(foo.getKey());

        // must be null
        if (foo1 != null) {
            throw new RuntimeException("Does not expect to find foo");
        }

        // get entry, expect to be found
        Entry bar1 = client.get(bar.getKey());
        if (bar1 == null) {
            throw new RuntimeException("error: cannot find bar entry.");
        }

        System.out.println("Verification passed.");

        // close kinetic client
        client.close();
    }

    public static void main(String[] args) throws KineticException,
            InterruptedException, UnsupportedEncodingException {

        BatchOperationAbortExample batch = new BatchOperationAbortExample();

        batch.run("localhost", 8123);
    }

}
