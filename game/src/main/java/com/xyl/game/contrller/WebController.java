package com.xyl.game.contrller;

import com.xyl.game.po.GridPage;
import com.xyl.game.po.Page;
import com.xyl.game.utils.FinalVariable;
import com.xyl.game.utils.HeapVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * WebController
 *
 * @author Naah
 * @date 2018-01-21
 */

@RestController
@CrossOrigin
public class WebController {

    /**
     * 获取题目倒计时秒数
     * @return
     */
    @GetMapping("intervalSecond")
    public Integer getIntervalSecond(){
        return HeapVariable.intervalSecond;
    }

    /**
     * 获取当前题目
     * @return
     */
    @GetMapping("/getQuestion")
    public GridPage getQuestion(){
        GridPage result=new GridPage();
        Page page=new Page();

        //如果游戏开始时间为空则返回没有游戏
        if (HeapVariable.beginTime==null) {
            result.setErrorCode(FinalVariable.NO_GAME_STATUS_CODE);
            result.setMessage(FinalVariable.NO_GAME_MESSAGE);
            return result;
        }
        //如果当前题目为空则返回没有题目
        if (HeapVariable.now==null) {
            result.setErrorCode(FinalVariable.NO_QUESTION_STATUS_CODE);
            result.setMessage(FinalVariable.NO_QUESTION_MESSAGE);
            return result;
        }
        //返回当前题目
        result.setErrorCode(FinalVariable.NORMAL_STATUS_CODE);
        result.setMessage(FinalVariable.NORMAL_MESSAGE);
        page.add(HeapVariable.now);
        result.setPageList(page);
        return result;

    }
}
