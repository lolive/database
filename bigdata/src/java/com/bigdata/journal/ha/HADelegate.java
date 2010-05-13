/**

Copyright (C) SYSTAP, LLC 2006-2010.  All rights reserved.

Contact:
     SYSTAP, LLC
     4501 Tower Road
     Greensboro, NC 27410
     licenses@bigdata.com

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; version 2 of the License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.bigdata.journal.ha;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.atomic.AtomicReference;

import com.bigdata.io.WriteCacheService;
import com.bigdata.journal.AbstractJournal;
import com.bigdata.journal.Environment;
import com.bigdata.journal.IBufferStrategy;
import com.bigdata.journal.IRootBlockView;

/**
 * The HADelegate provides the concrete implementation of the remote HAGlue
 * interface, delegated by the HADelegator class.
 * 
 * @author Martyn Cutcher
 * 
 */
public abstract class HADelegate {

	final protected Environment environment;

	public HADelegate(Environment environment) {
		this.environment = environment;
	}

	public InetAddress getWritePipelineAddr() {
		return environment.getWritePipelineAddr();
	}

	public int getWritePipelinePort() {
		return environment.getWritePipelinePort();
	}

	public abstract RunnableFuture<Void> abort2Phase(long token) throws IOException;

	public abstract RunnableFuture<Void> commit2Phase(final long commitTime) throws IOException;

	public abstract RunnableFuture<Boolean> prepare2Phase(final IRootBlockView rootBlock) throws IOException;

	public abstract RunnableFuture<ByteBuffer> readFromDisk(long token, long addr);

	public abstract RunnableFuture<Void> writeCacheBuffer(long fileExtent) throws IOException;

}
