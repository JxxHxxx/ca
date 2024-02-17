package com.jxx.ca.batch.job.commit.configuration;

import com.jxx.ca.batch.job.commit.processor.ActiveMemberProcessor;
import com.jxx.ca.batch.job.commit.processor.ActiveValidateProcessor;
import com.jxx.ca.batch.job.commit.processor.CommitCheckProcessor;
import com.jxx.ca.batch.job.commit.processor.RenewRepoProcessor;
import com.jxx.ca.batch.job.commit.reader.CommitReader;
import com.jxx.ca.batch.job.commit.model.CommitCheckModel;
import com.jxx.ca.batch.job.commit.model.RenewRepoModel;
import com.jxx.ca.batch.job.commit.reader.RenewRepoReader;
import com.jxx.ca.batch.job.commit.writer.CommitCheckWriter;
import com.jxx.ca.batch.job.commit.writer.RenewRepoWriter;
import com.jxx.ca.batch.generator.IdentifyJobParameterGenerator;
import com.jxx.ca.github.api.GithubRepoCommitHistoryApiAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CommitCheckJobConfiguration {
    private final DataSource dataSource;
    private final RowMapper<CommitCheckModel> rowMapper;
    private final RowMapper<RenewRepoModel> renewRepoModelRowMapper;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final GithubRepoCommitHistoryApiAdapter githubRepoCommitHistoryApiAdapter;

    @Bean(name = "commit-check.job")
    public Job commitCheckJob() {
        return jobBuilderFactory.get("commit-check.job")
                .start(renewRepoStep()).on(ExitStatus.COMPLETED.getExitCode()).to(commitCheckStep())
                .end()
                .incrementer(new IdentifyJobParameterGenerator())
                .build();
    }

    @Bean(name = "commit-check.step1.renew-repo")
    public Step renewRepoStep() {
        return stepBuilderFactory.get("commit-check.step1.renew-repo")
                .<RenewRepoModel, RenewRepoModel>chunk(10)
                .reader(renewRepoReader())
                .processor(renewRepoProcessors())
                .writer(renewRepoWriter())
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<RenewRepoModel> renewRepoReader() {
        return new RenewRepoReader(dataSource, renewRepoModelRowMapper).build();
    };

    @Bean
    @StepScope
    public CompositeItemProcessor<RenewRepoModel, RenewRepoModel> renewRepoProcessors() {
        List<ItemProcessor<RenewRepoModel, RenewRepoModel>> delegates = new ArrayList<>();
        delegates.add(new ActiveMemberProcessor());
        delegates.add(new RenewRepoProcessor());

        CompositeItemProcessor<RenewRepoModel, RenewRepoModel> compositeProcessor =
                new CompositeItemProcessor<>();

        compositeProcessor.setDelegates(delegates);
        return compositeProcessor;
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<RenewRepoModel> renewRepoWriter() {
        return new RenewRepoWriter(dataSource).build();
    }


    @Bean(name = "commit-check.step2.commit-check")
    public Step commitCheckStep() {
        return stepBuilderFactory.get("commit-check.step2.commit-check")
                .<CommitCheckModel, CommitCheckModel>chunk(10)
                .reader(commitCheckReader())
                .processor(commitCheckProcessors())
                .writer(commitCheckWriter())
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<CommitCheckModel> commitCheckReader() {
        return new CommitReader(dataSource, rowMapper).build();
    }

    @Bean
    @StepScope
    public CompositeItemProcessor<CommitCheckModel, CommitCheckModel> commitCheckProcessors() {
        List<ItemProcessor<CommitCheckModel, CommitCheckModel>> delegates = new ArrayList<>();
        delegates.add(new ActiveValidateProcessor());
        delegates.add(new CommitCheckProcessor(githubRepoCommitHistoryApiAdapter));

        CompositeItemProcessor<CommitCheckModel, CommitCheckModel> compositeProcessor =
                new CompositeItemProcessor<>();

        compositeProcessor.setDelegates(delegates);

        return compositeProcessor;
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<CommitCheckModel> commitCheckWriter() {
        return new CommitCheckWriter(dataSource).build();
    }
}

