package com.hana7.hanaro.member.dto;

import com.hana7.hanaro.member.entity.Auth;
import com.hana7.hanaro.member.entity.Member;
import java.time.LocalDateTime;

public record MemberAdminDTO(
	String email,
	String nickname,
	Auth auth,
	LocalDateTime createdAt,
	LocalDateTime deleteAt
) {
	public static MemberAdminDTO from(Member m) {
		return new MemberAdminDTO(
			 m.getEmail(), m.getNickname(),
			m.getAuth(), m.getCreatedAt(), m.getDeleteAt()
		);
	}
}
