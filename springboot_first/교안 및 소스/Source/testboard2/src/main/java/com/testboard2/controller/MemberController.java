package com.testboard2.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.testboard2.dto.MemberDTO;
import com.testboard2.service.MemberService;

@Controller
public class MemberController {

	
	/*
	 * DI
	 * 
	 */
	@Autowired
	private MemberService memberService;

	
	/*
	 * 회원 등록 Form 페이지 + 회원 수정 Form
	 * 
	 */
	@GetMapping("/member/memberWriteFormNew")
	public String memberWriteForm( 
			@RequestParam( value="num", required=false ) Integer num,
			Model model ) {
		
		if( num != null ) {
			
			System.out.println( num );
			MemberDTO m1 = memberService.getMemberOne( num );
			
			if( m1 == null ) {
				
				model.addAttribute( "msg", "회원 정보가 없습니다. 메인 페이지로 이동합니다." );
				model.addAttribute( "url", "/" );
				
				return "/member/messageAlert";  // messageAlert.html
			}
			
			// 잘 되는지 콘솔에 출력
			System.out.println( m1.getName() );
			System.out.println( m1.getId() );
			System.out.println( m1.getPhone() );
			
			// Form 페이지로 m1 객체를 전달 --> 모델(model)
			model.addAttribute( "memberDTO", m1 );
			model.addAttribute( "formTitle", "Modification" );
			model.addAttribute( "num", num );
			
		}
		else {
			
			System.out.println( "null 입니다." );
			
			// 등록 처리(신규 회원)
			model.addAttribute( "memberDTO", new MemberDTO() );
			model.addAttribute( "formTitle", "Registration" );
			
		}

		return "/member/memberWriteFormNew"; // memberWriteFormNew.html
	}

	
	/*
	 * 회원 등록 Ok
	 * 
	 */
	@PostMapping("/member/memberWriteOk")
	public String insertMember(
			MemberDTO m1, 
			Model model ) {

		try {
			// 등록 처리
			System.out.println(m1.getName());
			System.out.println(m1.getId());
			System.out.println(m1.getPhone());

			memberService.insertMember(m1);
			
			// 등록 안내 메시지 출력
			model.addAttribute( "msg", "회원 등록이 처리되었습니다. 메인 페이지로 이동합니다." );
			model.addAttribute( "url", "/" );
			
			return "/member/messageAlert";  // messageAlert.html
			
		} catch (Exception e) {
			// err
		}

		return "redirect:/";
	}
	

	/*
	 * 회원 수정 Ok
	 * 
	 */
	@PostMapping("/member/memberUpdateOk")
	public String updateMember(
			MemberDTO m1, 
			HttpServletRequest request, 
			Model model ) {
		
		String num_ = request.getParameter("num");
		int num = Integer.parseInt(num_);

		try {
			// 수정 처리
			System.out.println(m1.getName());
			System.out.println(m1.getId());
			System.out.println(m1.getPhone());
			System.out.println( "넘어온 번호는 = " + num );

			memberService.updateMember(m1);
			
			model.addAttribute( "msg", "회원 정보가 수정되었습니다. 확인 페이지로 이동합니다. ^.~" );
			model.addAttribute( "url", "/member/memberWriteFormNew?num=" + num );
			
			return "/member/messageAlert";  // messageAlert.html
			
		} catch (Exception e) {
			// err
		}

		return "redirect:/member/memberWriteFormNew?num=" + num;
	}
	
	
	/*
	 * 회원 리스트
	 * 
	 */
	@GetMapping("/member/memberList")
	public String memberList( Model model ) {
		
		List<MemberDTO> memberList = memberService.getMemberList();
		
		// 객체 리스트 전달 - 모델에 담아서 리스트 페이지로 전달
		model.addAttribute( "memberList", memberList );
		
		return "/member/memberList";  // memberList.html
	}
	
	
	/*
	 * 회원 삭제 Ok (생각보다 생각할게 많네..ㅠ.ㅠ)
	 * 
	 */
	@GetMapping("/member/memberDeleteOk")
	public String memberDeleteOk( @RequestParam( value="num", required=false ) Integer num, Model model ) {
		
		// null 체크
		if( num == null ) {
			System.out.println( "null 입니다." );
			return "redirect:/member/memberList";
		}
		System.out.println( num );
		
		// try .. catch ~
		try {
			int isOk = memberService.deleteMember(num);
			System.out.println( "isOk = " + isOk );
			
			if( isOk != 1 ) {
				System.out.println( "삭제 실패 = " + isOk );
				
				model.addAttribute( "msg", "회원 삭제가 실패되었습니다. 리스트로 이동합니다." );
				model.addAttribute( "url", "/member/memberList" );
			}
			else {
				System.out.println( "삭제 성공 = " + isOk );
				
				model.addAttribute( "msg", "회원 정보가 삭제되었습니다. 멤버 리스트 페이지로 이동합니다. ^.~" );
				model.addAttribute( "url", "/member/memberList" );
			}			
		}
		catch( DataAccessException e ) {
			// DB 처리시 문제가 있나???
			
		}
		catch( Exception e ) {
			// 시스템에 문제가 있나???
			
		}
		
		return "/member/messageAlert";  // messageAlert.html
	}
	
	
}

























