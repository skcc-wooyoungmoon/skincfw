package com.testboard2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testboard2.dto.MemberDTO;
import com.testboard2.mapper.MemberMapper;


@Service
public class MemberServiceImpl implements MemberService {
	
	
	/*
	 * DI
	 * 
	 */
	@Autowired
	private MemberMapper memberMapper;
	
	
	/*
	 * INSERT
	 * 
	 */
	@Override
	public void insertMember( MemberDTO memberDTO ) {
		
		memberMapper.insertMember( memberDTO );
	}
	

	/*
	 * SELECT MEMBER ONE
	 * 
	 */
	@Override
	public MemberDTO getMemberOne( int num ) {
		
		return memberMapper.selectMemberOne( num );
	}
	
	
	/*
	 * UPDATE
	 * 
	 */
	@Override
	public void updateMember( MemberDTO memberDTO ) {
		
		memberMapper.updateMember( memberDTO );
	}
	
	
	/*
	 * SELECT MEMBER ALL
	 * 
	 */
	@Override
	public List<MemberDTO> getMemberList() {
		
		return memberMapper.selectMemberAll();
	}
	
	
	/*
	 * DELETE MEMBER ONE
	 * 
	 */
	@Override
	public int deleteMember( int num ) {
		
		return memberMapper.deleteMemberOne( num );
	}
	
	
}























