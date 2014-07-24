package sh.wu.james.example.dao;

import org.springframework.stereotype.Repository;

import sh.wu.james.common.utils.Logger;
import sh.wu.james.example.dto.HelloworldDTO;

@Repository
public class HelloworldDAO {

    public void updateStatus(HelloworldDTO channelLoanRequest) {
        Logger.info(this, "Update Hello World Status.");
    }

    public HelloworldDTO getById(Long id) {
        Logger.info(this, "Return hellow world object with id:" + id);
        return null;
    }

    public Object insert(String string, HelloworldDTO req) {
        Logger.info(this, "Inert new Hello World Object");
        return req;
    }

}
