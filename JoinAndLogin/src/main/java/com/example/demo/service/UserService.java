package com.example.demo.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UserDto;
import com.example.demo.exception.ErrorCode;
import com.example.demo.exception.ErrorException;
import com.example.demo.exception.SignupResultFields;
import com.example.demo.mapper.UserMapper;

@Service
public class UserService{

	@Autowired
	UserMapper userMapper;
	
	// 회원가입 기능
	public int join(UserDto userDto)  {
		String regDate = userDto.getRegDate().replace('T', ' ');
		regDate +=":00";
		userDto.setRegDate(regDate);
		Date userBirthday = getUserBirthday(userDto.getRegDate());
		Date now = new Date();
		int result = 0;
		
		if(userBirthday.after(now)) {
			// BAD_REQUEST
			// 입력한 생년월일이 현재 시간보다 늦으면 에러 출력
			SignupResultFields s = new SignupResultFields("regDate","생년월일은 현재 시간보다 늦을 수 없습니다.");
			List<SignupResultFields> fields = new ArrayList<SignupResultFields>();
			fields.add(s);
			throw new ErrorException(ErrorCode.NUMBER_1002,"입력항목 오류",true,fields);
		}else {
			try {
				result = userMapper.join(userDto);
			} catch (Exception e) {
				e.printStackTrace();
				// 회원가입 실패 시 백업된 파일에 저장하기
				// 기존 백업파일에 이어 붙이기
				try {
					// 저장된 데이터 파일 읽어와 이어쓰기
					BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(createFile(),true));
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
					bufferedWriter.close();
					result=1;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return result;
	}

	// 아이디 중복확인
	public int idDuplicationCheck(String id) {
		int result=0;
		try {
			result = userMapper.idDuplicationCheck(id);
		} catch (Exception e) {
			e.printStackTrace();
			// 중복확인 실패 시 백업된 파일에서 정보 읽어오기
			File backupFile = createFile();
			try {
				FileReader reader = new FileReader(backupFile);	// 파일 읽어오기
				BufferedReader bufReader = new BufferedReader(reader);	// 파일을 한 줄씩 읽기 위해 사용
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
		}
		return result;
	}

	// 로그인 기능
	public UserDto login(UserDto userDto) {
		UserDto uDto = new UserDto();
		try {
			uDto = userMapper.login(userDto);
		} catch (Exception e) {
			// DB에서 읽어올 수 없을 시, 백업된 파일에서 읽어오기
			e.printStackTrace();
			File backupFile = createFile();
			try {
				FileReader reader = new FileReader(backupFile);	// 파일 읽어오기
				BufferedReader bufReader = new BufferedReader(reader);	// 파일을 한 줄씩 읽기 위해 사용
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
		}
		return uDto;
	}

	// 자동 로그인
	public void autoLogin(String id, String sessinKey, Timestamp sessionlimit) {
		try {
			userMapper.autoLogin(id, sessinKey, sessionlimit);
		} catch (Exception e) {
			// 실패 시 백업 파일에서 읽어오기 (라인 읽어와 비교, 슬래시로 구분지어 아이디 가져오기)
			File backupFile = createFile();
			try {
				FileReader reader = new FileReader(backupFile);	// 파일 읽어오기
				BufferedReader bufReader = new BufferedReader(reader);	// 파일을 한 줄씩 읽기 위해 사용
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
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(createFile(),false));
				bufferedWriter.write(fileAllText);
				bufferedWriter.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	// 쿠키 정보 있는 지 확인
	public UserDto checkCookieExpirationTime(String sessinKey) {
		UserDto userDto = new UserDto();
		try {
			userDto = userMapper.checkCookieExpirationTime(sessinKey);
		} catch (Exception e) {
			// 실패 시 백업 파일에서 읽어오기
			// 반환값 userDto
			File backupFile = createFile();
			try {
				FileReader reader = new FileReader(backupFile);	// 파일 읽어오기
				BufferedReader bufReader = new BufferedReader(reader);	// 파일을 한 줄씩 읽기 위해 사용
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
		}
		return userDto;
	}
	
	// 파일 읽기
	public File createFile() {
		File backupFile = new File("C:/backup/dataBackup.csv");
		return backupFile;
	}
	
	// 유저 생년월일 얻기
	public Date getUserBirthday(String regDate) {
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date userBirthday = null;
		try {
			userBirthday = transFormat.parse(regDate);
		} catch (ParseException e2) {
			e2.printStackTrace();
			// 기타 오류
			SignupResultFields s = new SignupResultFields("","알 수 없음");
			List<SignupResultFields> fields = new ArrayList<SignupResultFields>();
			fields.add(s);
			throw new ErrorException(ErrorCode.NUMBER_1003,"기타 오류",true,fields);
		}
		return userBirthday;
	}
}
