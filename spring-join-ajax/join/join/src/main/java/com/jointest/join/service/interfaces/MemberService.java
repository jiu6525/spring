package com.jointest.join.service.interfaces;

import com.jointest.join.dto.SignUpFormDTO;
import org.springframework.http.ResponseEntity;

public interface MemberService {
    ResponseEntity signup(SignUpFormDTO formDTO);
}
