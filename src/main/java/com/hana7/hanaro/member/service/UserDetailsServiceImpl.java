package com.hana7.hanaro.member.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hana7.hanaro.member.dto.UserDTO;
import com.hana7.hanaro.member.entity.Member;
import com.hana7.hanaro.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	private final MemberRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("*** Service.loadUserByUsername: " + username);
		Member member = repository.findByEmail(username);

		if (member == null) {
			throw new UsernameNotFoundException(username + " is Not Found!!");
		}
		UserDTO dto = new UserDTO(member.getEmail(), member.getPassword(), member.getNickname(),
			member.getAuth());
		System.out.println("dto = " + dto);
		return dto;
	}
}
