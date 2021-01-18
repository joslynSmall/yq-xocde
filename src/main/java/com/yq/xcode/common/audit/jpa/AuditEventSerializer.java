package com.yq.xcode.common.audit.jpa;

import com.yq.xcode.common.audit.AuditEvent;

public interface AuditEventSerializer {

	public String toString(AuditEvent evt);
}
