package com.jxx.ca.batch.commit.processor;

import com.jxx.ca.batch.commit.model.RenewRepoModel;
import org.springframework.batch.item.ItemProcessor;

public class ActiveMemberProcessor implements ItemProcessor<RenewRepoModel, RenewRepoModel> {

    @Override
    public RenewRepoModel process(RenewRepoModel renewRepoModel) throws Exception {
        boolean active = renewRepoModel.isActive();

        return active ? renewRepoModel : null;
    }
}
