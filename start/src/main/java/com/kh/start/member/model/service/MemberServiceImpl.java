package com.kh.start.member.model.service;
import java.util.HashMap;
import java.util.Map;
import javax.management.RuntimeErrorException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.kh.start.auth.model.vo.CustomUserDetails;
import com.kh.start.exception.MemberIdDuplicateException;
import com.kh.start.member.model.dao.MemberMapper;
import com.kh.start.member.model.dto.ChangePasswordDTO;
import com.kh.start.member.model.dto.MemberDTO;
import com.kh.start.member.model.vo.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	
	// Mapper를 의존성 주입을 받아서 써야함
	private final MemberMapper mapper;
	private final PasswordEncoder passwordEncoder;
	public void signUp(MemberDTO member) {
		// controller로 가서 타입 적기
		// Mapper
		
		// 유효성 검증
		MemberDTO searchedMember = mapper.getMemberByMemberId(member.getMemberId());
		// dao에서 갔다오면 MemberDTO로 줌
		
		if(searchedMember != null) {
			throw new MemberIdDuplicateException("이미 존재하는 아이디입니다.");	// 아이디가 있으면 예외발생 시킴
		}
		
		
		
		
		
		// 위 if문을 넘어갔으면 insert!
		// ROLE 칼럼에 내맘대로 받드시 값을 하나 만들어서 넣어줘야 함
		
		// 1. 비밀번호 암호화
		// 위에 의존성 주입!!
//		passwordEncoder.encode(member.getMemberPw()); 이렇게도 가능하지만 신경써서 해보자잉? => model.vo.Member
		// 2. Role(권한) 주기 => 관리자인지 사용자인지 뭔지
		
		Member memberValue = Member.builder()
								   .memberId(member.getMemberId())
								   .memberPw(passwordEncoder.encode(member.getMemberPw()))
								   .memberName(member.getMemberName())
								   /*.role("ROLE_내맘대로 적기")*/
								   .role("ROLE_USER")
								   .build();
		
		mapper.signUp(memberValue);
		// mapper에 매개변수 타입 member로 바꿔주로가기
		
		log.info("사용자 등록 성공 : {}", member );
		
	}
	@Override
	public void changePassword(ChangePasswordDTO passwordEntity) {
		
		// 현재 비밀번호를 맞게 입력했는지 검증
		// 맞다면 새로운 패스워드를 암호화
		// SecurityContextHolder에서 사용자 정보 받기(어떤 사용자가 비번 바꿔달라고 하는지)
		
		// 맞게 썼는지 검증을 위해 있어야 할것
		// -> PasswordEncoder를 가지고 matches() 호출
		// 첫번째 인자 : 평문, 두번째 인자 : 암호문
		// (암호문은 SecurityHodler 안에 principal이ㅆ는데 이 안에 보면은 ~~~)
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		CustomUserDetails user = (CustomUserDetails)auth.getPrincipal();
//		if(!passwordEncoder.matches(passwordEntity.getCurrentPassword(), user.getPassword())) {
//			throw new RuntimeException("비밀번호가 일치하지 않습니다!");
//			
//		}
		
		Long memberNo = passwordMatches(passwordEntity.getCurrentPassword());
		
		String encodedPassword = passwordEncoder.encode(passwordEntity.getNewPassword());
		//Long memberNo = user.getMemberNo(); // 사용자의 식별값
		// 이제 이 두개를 mapper에 보내야하는데
		// Map에 담자
		
		/* String encodedPassword = passwordEncoder */
		Map<String, Object> changeRequest = new HashMap();
		changeRequest.put("memberNo", memberNo);
		changeRequest.put("encodedPassword", encodedPassword);
		
		
		// 매퍼에 가서 UPDATE
		// UPDATE TB_BOOT_MEMBER MEMBER_PW = ? WHERE MEMBER_NO/ID = 현재요청한사용자의식별값
		mapper.changePassword(changeRequest);
		
	}
	@Override
	public void deleteByPassword(String password) {
		
		// 유효성 검증은 생략
		// 사용자가 입력한 비밀번화와 DB에 저장된 비밀번호가 둘이 짝짝꿍된게 맞는지~~
		
		// 확인 다 된 후, Mapper가서 -> DELETE
		// DELETE FROM TB_BOOT_MEMBER WHERE MEMBER_NO = ?
		// 입력된거는 password지만 실제 delete하려면 pk가 필요함
		// => SELECT를 해야해
		
		// 버그yml기록 보면 "이게오나?"가 오기전에 이미 select를 함
		
		// 이제 사용자 인증/인가에 관한 모든 코드는 안짜도 됨
		// 즉, 이미 필터를 거치면서 Security ~~~에 다 담음
		
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		CustomUserDetails user = (CustomUserDetails)auth.getPrincipal();
//		if(!passwordEncoder.matches(passwordEntity.getCurrentPassword(), user.getPassword())) {
//			throw new RuntimeException("비밀번호가 일치하지 않습니다!");
//			
//		}
		// 중복되므로 분히!!!
		
		Long memberNo = passwordMatches(password);
		mapper.deleteByPassword(memberNo);
		
		
	}
	
	private Long passwordMatches(String password) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user = (CustomUserDetails)auth.getPrincipal();
		if(!passwordEncoder.matches(password,user.getPassword())) {
			throw new RuntimeException("비밀번호가 일치하지 않습니다!");
			
		}
		return user.getMemberNo();
	
	
	
	}
	
	
}