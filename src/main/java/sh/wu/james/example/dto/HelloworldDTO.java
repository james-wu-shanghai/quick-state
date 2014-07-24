package sh.wu.james.example.dto;

import sh.wu.james.example.statemachine.BizOperations;

/**
 * 来自渠道的借款请求（控制部分）
 * 
 * @author wujian
 * 
 */
public class HelloworldDTO {
    /**
	 * 
	 */
    private static final long serialVersionUID = 4508607291569442802L;

    private BizOperations state;
    private HelloworldStatus status;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BizOperations getState() {
        return state;
    }

    public HelloworldStatus getStatus() {
        return status;
    }

    public void setStatus(HelloworldStatus status) {
        this.status = status;
    }

    public void setState(BizOperations state) {
        this.state = state;
    }

}
