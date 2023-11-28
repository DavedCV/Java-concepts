package com.example.springbootbigquery.service;

import com.example.springbootbigquery.exceptions.BigQueryManualException;
import com.google.cloud.bigquery.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BigQueryService {
    BigQuery bigquery;
    @Autowired
    public BigQueryService(BigQuery bigquery) {
        this.bigquery = bigquery;
    }

    public List<String> queryCountries() throws BigQueryManualException {
        String query = "SELECT DISTINCT d.country_name" +
                " FROM `bigquery-public-data.world_bank_intl_education.international_education` d" +
                " INNER JOIN `bigquery-public-data.world_bank_intl_education.series_summary` s" +
                " ON d.indicator_code = s.series_code" +
                " ORDER BY d.country_name;";
        QueryJobConfiguration queryJobConfiguration = QueryJobConfiguration.newBuilder(query).build();

        JobId jobId = JobId.of(UUID.randomUUID().toString());
        Job queryJob = bigquery.create(JobInfo.newBuilder(queryJobConfiguration).setJobId(jobId).build());

        try {
            queryJob = queryJob.waitFor();


            if (queryJob == null) {
                throw new BigQueryManualException("Job no longer exists");
            } else if (queryJob.getStatus().getError() != null) {
                throw new BigQueryManualException(queryJob.getStatus().getError().toString());
            }

            TableResult result = queryJob.getQueryResults();

            return result.streamValues()
                         .map(row -> row.get("country_name").getStringValue())
                         .toList();

        } catch (InterruptedException e) {
            throw new BigQueryManualException("InterruptedException - query job failed", e);
        }
    }
}
