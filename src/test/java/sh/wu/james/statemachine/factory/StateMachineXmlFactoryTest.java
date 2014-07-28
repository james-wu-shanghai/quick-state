package sh.wu.james.statemachine.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import sh.wu.james.common.statemachine.GenericStateMachine;
import sh.wu.james.common.statemachine.factory.StateMachineXmlFactory;
import sh.wu.james.common.utils.ReflectionUtil;
import sh.wu.james.example.dto.HelloworldDTO;
import sh.wu.james.example.dto.HelloworldStatus;
import sh.wu.james.example.statemachine.BizOperations;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@TransactionConfiguration(defaultRollback = true)
public class StateMachineXmlFactoryTest {

    @Resource
    StateMachineXmlFactory<BizOperations, HelloworldDTO, HelloworldStatus> testee;

    @Test
    public void initState() {
        HelloworldDTO req = new HelloworldDTO();
        BizOperations state = testee.initState(req);
        System.out.println(state);
        state = state.wakeUp();
        state = state.eatBreakfast();
        state = state.eatLunch();
        state = state.sleep();

        assertEquals("end", (ReflectionUtil.getValue(ReflectionUtil.getValue(state, "h"), "stateName")));
    }

    @Test
    public void useGenericState() {
        HelloworldDTO req = new HelloworldDTO();
        GenericStateMachine state = (GenericStateMachine) testee.initState(req);
        Object payload = state.getPayload();
        assertTrue(payload instanceof HelloworldDTO);

    }
}
