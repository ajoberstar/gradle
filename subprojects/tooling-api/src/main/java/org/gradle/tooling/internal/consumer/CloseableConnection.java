/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.tooling.internal.consumer;

import org.gradle.tooling.internal.protocol.BuildParametersVersion1;
import org.gradle.tooling.internal.protocol.ConnectionVersion4;
import org.gradle.tooling.internal.protocol.ProjectVersion3;
import org.gradle.tooling.internal.protocol.ResultHandlerVersion1;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Decorates a connection to apply close semantics.
 *
 * Note that this is a dummy implementation for now.
 */
class CloseableConnection implements ConnectionVersion4 {
    private final ConnectionVersion4 connection;
    private AtomicBoolean closed = new AtomicBoolean();

    public CloseableConnection(ConnectionVersion4 connection) {
        this.connection = connection;
    }

    public String getDisplayName() {
        return connection.getDisplayName();
    }

    public void executeBuild(BuildParametersVersion1 buildParameters, ResultHandlerVersion1<? super Void> handler) throws IllegalStateException {
        assertOpen();
        connection.executeBuild(buildParameters, handler);
    }

    public <T extends ProjectVersion3> void getModel(Class<T> type, ResultHandlerVersion1<? super T> handler) throws UnsupportedOperationException, IllegalStateException {
        assertOpen();
        connection.getModel(type, handler);
    }

    private void assertOpen() {
        if (closed.get()) {
            throw new IllegalStateException("This connection has been closed.");
        }
    }

    public void stop() {
        if (!closed.getAndSet(true)) {
            connection.stop();
        }
    }
}
