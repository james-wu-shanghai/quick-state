package sh.wu.james.example.statemachine.judger;

import org.springframework.stereotype.Service;

import sh.wu.james.common.statemachine.GenericStateMachine;
import sh.wu.james.common.statemachine.fork.Judger;

@Service
public class CreateJudger implements Judger {

	@Override
	public String judge(GenericStateMachine state) {
		return "canCreate";
	}

}
