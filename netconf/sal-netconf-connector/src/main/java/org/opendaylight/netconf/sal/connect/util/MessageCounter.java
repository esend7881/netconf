/*
 * Copyright (c) 2014, 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.netconf.sal.connect.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageCounter {
    final AtomicInteger messageId = new AtomicInteger(0);

    private static final String MESSAGE_ID_BLUEPRINT = "%s-%s";

    public String getNewMessageId(final String prefix) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(prefix), "Null or empty prefix");
        return String.format(MESSAGE_ID_BLUEPRINT, prefix, getNewMessageId());
    }

    public String getNewMessageId() {
        return Integer.toString(messageId.getAndIncrement());
    }
}
