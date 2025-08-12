package com.hana7.hanaro.member.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hana7.hanaro.member.dto.MemberAdminDTO;
import com.hana7.hanaro.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/members")
@Tag(name = "[관리자] 회원관리")
@RequiredArgsConstructor
public class AdminController {
	private final MemberService memberService;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	@Operation(summary = "회원 목록 조회", description = "기본은 삭제되지 않은 회원만. includeDeleted=true면 삭제회원 포함")
	public ResponseEntity<List<MemberAdminDTO>> list(
		@RequestParam(defaultValue = "false") boolean includeDeleted
	) {
		return ResponseEntity.ok(memberService.list(includeDeleted));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{memberId}")
	@Operation(summary = "회원 소프트 삭제", description = "deleteAt에 현재 시간이 기록됩니다.")
	public ResponseEntity<Void> delete(@PathVariable Long memberId) {
		memberService.softDelete(memberId);
		return ResponseEntity.noContent().build();
	}
}
