package sh.wu.james.example.statemachine.listener;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import sh.wu.james.common.statemachine.GenericStateMachine;
import sh.wu.james.common.statemachine.listener.StateListener;
import sh.wu.james.common.utils.Logger;
import sh.wu.james.example.dao.HelloworldDAO;
import sh.wu.james.example.dto.HelloworldDTO;
import sh.wu.james.example.dto.HelloworldStatus;
import sh.wu.james.example.statemachine.BizOperations;

@Service
public class SleepListener implements StateListener<BizOperations, HelloworldDTO, HelloworldStatus> {
    @Resource
    private HelloworldDAO helloworldDao;

    @Override
    public void onEvent(GenericStateMachine<BizOperations, HelloworldDTO, HelloworldStatus> state, String evtType) {
        if ("sleeping".equals(evtType)) {
            Logger.info(this, "Sleeping");
        }

    }

}
