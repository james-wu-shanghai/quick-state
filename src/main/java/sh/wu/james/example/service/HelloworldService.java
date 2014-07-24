package sh.wu.james.example.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import sh.wu.james.common.statemachine.GenericStateMachine;
import sh.wu.james.common.statemachine.factory.StateMachineXmlFactory;
import sh.wu.james.example.dao.HelloworldDAO;
import sh.wu.james.example.dto.HelloworldDTO;
import sh.wu.james.example.dto.HelloworldStatus;
import sh.wu.james.example.statemachine.BizOperations;

@Service
public class HelloworldService {
    @Resource
    private HelloworldDAO helloworldDao;
    @Resource
    private StateMachineXmlFactory<BizOperations, HelloworldDTO, HelloworldStatus> factory;

    public HelloworldDTO insert(Long id) {
        HelloworldDTO req = new HelloworldDTO();
        BizOperations state = factory.initState(req);
        BizOperations create = state.create();
        return ((GenericStateMachine<BizOperations, HelloworldDTO, HelloworldStatus>) create).getPayload();
    }
}
