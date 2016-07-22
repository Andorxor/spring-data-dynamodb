package org.socialsignin.spring.data.dynamodb.domain.sample;

import java.util.Date;

import org.springframework.data.annotation.Id;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBNativeBoolean;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "feed_user")
public class FeedUser {
    @Id
    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    private String id;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "idx_global_usrNo_feedOpenYn")
    private int usrNo;

    @DynamoDBAttribute
    private String feedId;

    @DynamoDBAttribute
    private Date feedRegDate;

    @DynamoDBAttribute
    @DynamoDBNativeBoolean
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "idx_global_usrNo_feedOpenYn")
    private boolean feedOpenYn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUsrNo() {
        return usrNo;
    }

    public void setUsrNo(int usrNo) {
        this.usrNo = usrNo;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public Date getFeedRegDate() {
        return feedRegDate;
    }

    public void setFeedRegDate(Date feedRegDate) {
        this.feedRegDate = feedRegDate;
    }

    public boolean isFeedOpenYn() {
        return feedOpenYn;
    }

    public void setFeedOpenYn(boolean feedOpenYn) {
        this.feedOpenYn = feedOpenYn;
    }
}