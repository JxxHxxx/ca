package com.jxx.ca.batch.commit.job;


import com.jxx.ca.batch.commit.processor.ActiveValidateProcessor;
import com.jxx.ca.batch.commit.processor.CommitCheckProcessor;
import com.jxx.ca.batch.commit.reader.CommitReader;
import com.jxx.ca.batch.commit.reader.CommitCheckModel;
import com.jxx.ca.batch.commit.writer.CommitCheckWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = "commit.check.job")
    public Job job() {
        return jobBuilderFactory.get("commit.check.job")
                .start(step())
                .incrementer(new ComplexIncrementer())
                .build();
    }

    @Bean(name = "commit.check.step1")
    public Step step() {
        return stepBuilderFactory.get("commit.check.step1")
                .<CommitCheckModel, CommitCheckModel>chunk(3)
                .reader(reader())
                .processor(compositeProcessor())
                .writer(writer())
                .build();
    }
    @Bean
    @StepScope
    public JdbcCursorItemReader<CommitCheckModel> reader() {
        return new CommitReader(dataSource, rowMapper).build();
    }

    @Bean
    @StepScope
    public CompositeItemProcessor<CommitCheckModel, CommitCheckModel> compositeProcessor() {
        List<ItemProcessor<CommitCheckModel, CommitCheckModel>> delegates = new ArrayList<>();
        delegates.add(new ActiveValidateProcessor());
        delegates.add(new CommitCheckProcessor());

        CompositeItemProcessor<CommitCheckModel, CommitCheckModel> compositeProcessor =
                new CompositeItemProcessor<>();

        compositeProcessor.setDelegates(delegates);

        return compositeProcessor;
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<CommitCheckModel> writer() {
        return new CommitCheckWriter(dataSource).build();
    }
}

