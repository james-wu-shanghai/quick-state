package sh.wu.james.example.statemachine.listener;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import sh.wu.james.common.statemachine.GenericState;
import sh.wu.james.common.statemachine.listener.StateListener;
import sh.wu.james.example.dao.HelloworldDAO;
import sh.wu.james.example.dto.HelloworldDTO;
import sh.wu.james.example.dto.HelloworldStatus;
import sh.wu.james.example.statemachine.BizOperations;
import sh.wu.james.example.statemachine.StateEventType;

@Service
public class ChangeStatusListener implements StateListener<BizOperations, HelloworldDTO, HelloworldStatus> {
    @Resource
    private HelloworldDAO helloworldDao;

    @Override
    public void onEvent(GenericState<BizOperations, HelloworldDTO, HelloworldStatus> state, String evtType) {

        if (StateEventType.STATUS_CHANGE.name().equals(evtType)) {
            HelloworldDTO req = state.getPayload();
            HelloworldStatus next = state.getNextStatus();
            req.setStatus(next);
            helloworldDao.updateStatus(req);
        }

    }

}
