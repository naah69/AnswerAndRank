package com.xyl.game.po;

import org.apache.commons.net.ntp.TimeStamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Answer
 *
 * @author Naah
 * @date 2018-01-21
 */
@Getter
@Setter
@AllArgsConstructor
public class Answer {
    private Integer index;
    private AnnualMeetingGameQuestion question;
    private Byte answer;
    private Integer time;
    private Boolean isRight;
     private TimeStamp commitTime;
    @Override
    public String toString() {
    	StringBuffer sb = new StringBuffer();
    	Character[] indexc = new Character[2];
    	String[] data = {""+index,answer+""};
    	for(int i = 0 ; i < indexc.length ; i++){
    		switch(data[i]){
    		case "1":
    			indexc[i] = 'A';
    			break;
    		case "2":
    			indexc[i] = 'B';
    			break;
    		case "3":
    			indexc[i] = 'C';
    			break;
    		case "4":
    			indexc[i] = 'D';
    			break;
    		default:
    			indexc[i] = ' ';
    	}
    	}
    	
    	sb.append("题号:"+indexc[0]+";");
    	sb.append("用户回答"+indexc[1]+";");
    	sb.append("使用答题时间："+time+";");
    	String isRightStr = ""; 
    	if(isRight){
    		isRightStr="是";
    	} else {
    		isRightStr="否";
    	}
    	
    	sb.append("是否答对："+isRightStr+";");
    	
    	return sb.toString();
    }
}
