package com.xyl.game.contrller;

import com.xyl.game.Service.UploadScoreService;
import com.xyl.game.dto.QuestionDTO;
import com.xyl.game.po.GridPage;
import com.xyl.game.po.Page;
import com.xyl.game.po.User;
import com.xyl.game.utils.FinalVariable;
import com.xyl.game.utils.HeapVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * uploadSocreController
 *
 * @author Naah
 * @date 2018-01-21
 */
@SuppressWarnings("ALL")
@CrossOrigin
@RestController
public class UploadSocreController {

    @Autowired
    private UploadScoreService service;

    @PostMapping("uploadScore")
    public GridPage<QuestionDTO> uploadScore(Integer id,Byte answer, Integer times,HttpServletRequest request){

        String sessionId = request.getSession().getId();
        User user = HeapVariable.usersMap.get(sessionId);

        GridPage<QuestionDTO> result = service.uploadScore(id, answer, times, sessionId, user);

        return result;
    }

    @GetMapping("/getQuestion")
    public GridPage getQuestion(){
        GridPage result=new GridPage();
        Page page=new Page();
        if (HeapVariable.beginTime==null) {
            result.setErrorCode(FinalVariable.NO_GAME_STATUS_CODE);
            result.setMessage(FinalVariable.NO_GAME_MESSAGE);
            return result;
        }
        if (HeapVariable.now==null) {
            result.setErrorCode(FinalVariable.NO_QUESTION_STATUS_CODE);
            result.setMessage(FinalVariable.NO_QUESTION_MESSAGE);
            return result;
        }
        result.setErrorCode(FinalVariable.NORMAL_STATUS_CODE);
        result.setMessage(FinalVariable.NORMAL_MESSAGE);
        page.add(HeapVariable.now);
        result.setPageList(page);
        return result;

    }
}
