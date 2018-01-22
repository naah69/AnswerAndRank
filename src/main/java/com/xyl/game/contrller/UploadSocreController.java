package com.xyl.game.contrller;

import com.xyl.game.Service.UploadScoreService;
import com.xyl.game.dto.QuestionDTO;
import com.xyl.game.po.GridPage;
import com.xyl.game.po.User;
import com.xyl.game.utils.HeapVariable;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController
public class UploadSocreController {

    @Autowired
    private UploadScoreService service;

    @PostMapping("uploadScore")
    public GridPage<QuestionDTO> uploadScore(Integer id,String answer, Integer times,HttpServletRequest request){

        String sessionId = request.getSession().getId();
        User user = HeapVariable.usersMap.get(sessionId);

        GridPage<QuestionDTO> result = service.uploadScore(id, answer, times, sessionId, user);

        return result;
    }
}
