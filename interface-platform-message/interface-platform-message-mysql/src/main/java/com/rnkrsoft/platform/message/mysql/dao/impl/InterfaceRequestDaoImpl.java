package com.rnkrsoft.platform.message.mysql.dao.impl;

import com.rnkrsoft.framework.orm.Pagination;
import com.rnkrsoft.platform.message.mysql.dao.InterfaceRequestDao;
import com.rnkrsoft.platform.message.mysql.entity.InterfaceRequestEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by woate on 2019/1/26.
 */
public class InterfaceRequestDaoImpl implements InterfaceRequestDao {
    @Override
    public int updateStatusAndRspNo(@Param("requestNo") String requestNo, @Param("expectOrderStatus") Integer expectOrderStatus, @Param("orderStatus") Integer orderStatus, @Param("responseNo") String responseNo) {
        return 0;
    }

    @Override
    public int updateStatus(@Param("requestNo") String requestNo, @Param("expectOrderStatus") Integer expectOrderStatus, @Param("orderStatus") Integer orderStatus) {
        return 0;
    }

    @Override
    public int countAll() {
        return 0;
    }

    @Override
    public int countAnd(InterfaceRequestEntity interfaceRequestEntity) {
        return 0;
    }

    @Override
    public int countOr(InterfaceRequestEntity interfaceRequestEntity) {
        return 0;
    }

    @Override
    public int deleteAnd(InterfaceRequestEntity interfaceRequestEntity) {
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(String s) {
        return 0;
    }

    @Override
    public int deleteOr(InterfaceRequestEntity interfaceRequestEntity) {
        return 0;
    }

    @Override
    public int deleteRuntime(InterfaceRequestEntity interfaceRequestEntity) {
        return 0;
    }

    @Override
    public int insert(InterfaceRequestEntity interfaceRequestEntity) {
        return 0;
    }

    @Override
    public int insertSelective(InterfaceRequestEntity interfaceRequestEntity) {
        return 0;
    }

    @Override
    public List<InterfaceRequestEntity> lockByForUpdateAnd(InterfaceRequestEntity interfaceRequestEntity) {
        return null;
    }

    @Override
    public InterfaceRequestEntity lockByForUpdateByPrimaryKey(String s) {
        return null;
    }

    @Override
    public List<InterfaceRequestEntity> lockByForUpdateOr(InterfaceRequestEntity interfaceRequestEntity) {
        return null;
    }

    @Override
    public int lockByUpdateSetPrimaryKey(String s) {
        return 0;
    }

    @Override
    public List<InterfaceRequestEntity> selectAll() {
        return null;
    }

    @Override
    public List<InterfaceRequestEntity> selectAnd(InterfaceRequestEntity interfaceRequestEntity) {
        return null;
    }

    @Override
    public InterfaceRequestEntity selectByPrimaryKey(String s) {
        return null;
    }

    @Override
    public List<InterfaceRequestEntity> selectOr(InterfaceRequestEntity interfaceRequestEntity) {
        return null;
    }

    @Override
    public Pagination<InterfaceRequestEntity> selectPageAnd(Pagination<InterfaceRequestEntity> pagination) {
        return null;
    }

    @Override
    public Pagination<InterfaceRequestEntity> selectPageOr(Pagination<InterfaceRequestEntity> pagination) {
        return null;
    }

    @Override
    public List<InterfaceRequestEntity> selectRuntime(String s) {
        return null;
    }

    @Override
    public int updateByPrimaryKey(InterfaceRequestEntity interfaceRequestEntity) {
        return 0;
    }

    @Override
    public int updateByPrimaryKeySelective(InterfaceRequestEntity interfaceRequestEntity) {
        return 0;
    }
}
