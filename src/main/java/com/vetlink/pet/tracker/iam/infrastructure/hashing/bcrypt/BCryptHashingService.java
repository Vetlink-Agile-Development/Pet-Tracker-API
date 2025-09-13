package com.vetlink.pet.tracker.iam.infrastructure.hashing.bcrypt;

import com.vetlink.pet.tracker.iam.application.internal.outboundservices.hashing.HashingService;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface BCryptHashingService extends HashingService, PasswordEncoder {
}
