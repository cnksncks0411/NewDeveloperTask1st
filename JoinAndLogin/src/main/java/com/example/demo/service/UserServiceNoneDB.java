package com.example.demo.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.example.demo.dto.UserDto;
import com.example.demo.exception.ErrorCode;
import com.example.demo.exception.ErrorException;
import com.example.demo.exception.SignupResultFields;
import com.example.demo.mapper.UserMapper;

@Service
public class UserServiceNoneDB implements UserDataService{

	@Autowired
	UserMapper userMapper;
	@Autowired
	UtilCollection utilCollection;
	
	// 회원가입 기능
	@Override
	public int join(UserDto userDto)  {
		// 유저 생년월일 Timestamp 포맷으로 변환하여 저장
		utilCollection.setUserBirthday(userDto);
		// Timestamp 포맷으로 변환 후 유저의 생년월일 가져오기
		Date userBirthday = utilCollection.getUserBirthday(userDto.getRegDate());
		// 비교할 현재 시간
		Date now = new Date();
		int result = 0;
		
		if(userBirthday.after(now)) {
			// BAD_REQUEST
			// 입력한 생년월일이 현재 시간보다 늦으면 에러 출력
			List<SignupResultFields> fields = utilCollection.getfields("생년월일","생년월일은 현재 시간보다 늦을 수 없습니다.");
			throw new ErrorException(ErrorCode.NUMBER_1002,"입력항목 오류",true,fields);
		}else {
			// 기존 백업파일에 이어 붙이기
			try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(utilCollection.createFile(),true));){
				// 저장된 데이터 파일 읽어와 이어쓰기
				bufferedWriter.newLine();
				bufferedWriter.write("\""+userDto.getId()+"\""
								+"/\""+userDto.getPw()+"\""
								+"/\""+userDto.getName()+"\""
								+"/"+"\"\""
								+"/"+"\"\""
								+"/"+"\"\""
								+"/"+"\"none\""
								+"/"+"\"\""
								+"/\""+userDto.getLevel()+"\""
								+"/\""+userDto.getDesc()+"\""
								+"/\""+userDto.getRegDate()+"\"");
				result=1;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return result;
	}

	// 회원가입 시 유효성 체크
	@Override
	public Map<String, String> validateHandling(Errors errors){
		Map<String, String> validatorResult = new HashMap<>();

		for(FieldError error : errors.getFieldErrors()) {
			String validKeyName = String.format("valid_%s", error.getField());
			validatorResult.put(validKeyName, error.getDefaultMessage());
		}
		return validatorResult;
	}
	
	// 아이디 중복확인
	@Override
	public int idDuplicationCheck(String id) {
		int result=0;
		// 백업된 파일에서 아이디 정보 읽어오기
		File backupFile = utilCollection.createFile();
		try (FileReader reader = new FileReader(backupFile);
			BufferedReader bufReader = new BufferedReader(reader);){
			
			String line = "";
			while((line=bufReader.readLine())!=null){
				String[] list = line.split("/");
				// list[0]=id
				if(list[0].equals("\""+id+"\"")) {	// where id=#{id}와 같은 표현
					result=1;
					break;
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return result;
	}

	// 로그인 기능
	@Override
	public UserDto login(UserDto userDto) {
		UserDto uDto = null;
		// 백업된 파일에서 로그인 정보 읽어오기
		File backupFile = utilCollection.createFile();
		try (FileReader reader = new FileReader(backupFile);
			BufferedReader bufReader = new BufferedReader(reader);) {

			String line = "";
			while((line=bufReader.readLine())!=null){
				String[] list = line.split("/");
				if(list[0].equals("\""+userDto.getId()+"\"")) {
					// list[0]=id, list[1]=pw, list[2]=name, list[8]=level, list[9]=desc, list[10]=regDate
					uDto = new UserDto(list[0],list[1],list[2],list[8],list[9],list[10],userDto.getAutoLogin());
					break;
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return uDto;
	}

	// 자동 로그인
	@Override
	public void autoLogin(String id, String sessinKey, Timestamp sessionlimit) {
		// 쿠키 정보 파일에서 읽어오기 (라인 읽어와 비교, 슬래시로 구분지어 아이디 가져오기)
		File backupFile = utilCollection.createFile();
		try (FileReader reader = new FileReader(backupFile);
			BufferedReader bufReader = new BufferedReader(reader);
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(utilCollection.createFile(),false));) {
			
			String line = "";	//한 줄씩 읽어올 내용
			String fileAllText="";	// 파일 update를 위한 전체 파일 내용
			while((line=bufReader.readLine())!=null){
				String[] list = line.split("/");
				// list[0]=id, list[6]=sessionKey, list[7]=sessionLimit
				if(list[0].equals(id)) {
					list[6]="\""+sessinKey+"\"";
					list[7]="\""+String.valueOf(sessionlimit)+"\"";
					String updateText = list[0];
					// 해당 id의 sessionKey, sessionLimit 수정
					for(int i=1; i<list.length; i++) {
						updateText+="/"+list[i];
					}
					fileAllText += updateText+System.lineSeparator();	// System.lineSeparator() = 줄바꿈
					break;
				}else {
					fileAllText += line+System.lineSeparator();
				}
			}
			// 파일 전체내용 덮어쓰기
			bufferedWriter.write(fileAllText);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	// 쿠키 정보 있는 지 확인
	@Override
	public UserDto checkCookieExpirationTime(String sessinKey) {
		UserDto userDto = null;
			// 쿠키 정보 존재 하는 지 파일에서 읽어오기
		File backupFile = utilCollection.createFile();
		try (FileReader reader = new FileReader(backupFile);
				BufferedReader bufReader = new BufferedReader(reader);){
			String line = "";
			
			while((line=bufReader.readLine())!=null){
				String[] list = line.split("/");
				// 쿠키 정보 확인을 위해 sessinKey 비교
				// list[6] = sessionKey
				if(list[6].equals("\""+sessinKey+"\"")) {
					// list[0]=id, list[1]=pw, list[2]=name, list[8]=level, list[9]=desc, list[10]=regDate
					userDto = new UserDto(list[0],list[1],list[2],list[8],list[9],list[10],true);
					break;
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return userDto;
	}
}
