package org.codecrafterslab.unity.response;

import org.codecrafterslab.unity.exception.core.BizException;
import org.codecrafterslab.unity.exception.core.BizStatus;
import org.codecrafterslab.unity.response.api.PageResult;
import org.codecrafterslab.unity.response.api.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2022/07/24 01:02
 */
public class ResultTest {

    @Test
    void success() {
        Result<?> success = ResultUtils.success();
        Assertions.assertTrue(success.isSuccess());
        Assertions.assertEquals(0, success.getCode());
        Assertions.assertEquals("success", success.getMessage());
        Assertions.assertNull(success.getData());
    }

    @Test
    void successData() {
        Result<List<Integer>> success = ResultUtils.success(Arrays.asList(1, 2, 3, 4, 5));
        Assertions.assertTrue(success.isSuccess());
        Assertions.assertEquals(0, success.getCode());
        Assertions.assertEquals("success", success.getMessage());
        Assertions.assertNotNull(success.getData());
    }

    @Test
    void successDataTotal() {
        PageResult<List<Integer>> success = ResultUtils.success(Arrays.asList(1, 2, 3, 4, 5), 10);
        Assertions.assertTrue(success.isSuccess());
        Assertions.assertEquals(0, success.getCode());
        Assertions.assertNull(success.getMessage());
        Assertions.assertNotNull(success.getData());
        Assertions.assertEquals(5, success.getData().size());
        Assertions.assertEquals(10, success.getTotal());
    }

    @Test
    void successMessageData() {
        Result<List<Integer>> success = ResultUtils.success("测试成功", Arrays.asList(1, 2, 3, 4, 5));
        Assertions.assertTrue(success.isSuccess());
        Assertions.assertEquals(0, success.getCode());
        Assertions.assertEquals("测试成功", success.getMessage());
        Assertions.assertNotNull(success.getData());
    }

    @Test
    void successMessageDataTotal() {
        PageResult<List<Integer>> success = ResultUtils.success("测试成功", Arrays.asList(1, 2, 3, 4, 5), 20);
        Assertions.assertTrue(success.isSuccess());
        Assertions.assertEquals(0, success.getCode());
        Assertions.assertEquals("测试成功", success.getMessage());
        Assertions.assertNotNull(success.getData());
        Assertions.assertEquals(5, success.getData().size());
        Assertions.assertNotNull(success.getTotal());
        Assertions.assertEquals(20, success.getTotal());
    }

    @Test
    void failureCodeMessageData() {
        Result<String> failure = ResultUtils.failure(1, "测试异常提示", "测试异常数据");
        Assertions.assertFalse(failure.isSuccess());
        Assertions.assertNotEquals(0, failure.getCode());
        Assertions.assertNotEquals("success", failure.getMessage());
        Assertions.assertNotNull(failure.getData());
    }

    @Test
    void failureCodeMessage() {
        Result<Object> failure = ResultUtils.failure(1, "测试异常提示");
        Assertions.assertFalse(failure.isSuccess());
        Assertions.assertNotEquals(0, failure.getCode());
        Assertions.assertNotEquals("success", failure.getMessage());
        Assertions.assertNull(failure.getData());
    }

    @Test
    void failureExceptionData() {
        Result<String> failure = ResultUtils.failure(new Exception("任意异常"), "测试异常类");
        Assertions.assertFalse(failure.isSuccess());
        Assertions.assertEquals(BizStatus.INTERNAL_SERVER_ERROR.getCode(), failure.getCode());
        Assertions.assertEquals("任意异常", failure.getMessage());
        Assertions.assertNotNull(failure.getData());
    }

    @Test
    void failureException() {
        BizException bizException = new BizException(BizStatus.ACCOUNT_EXPIRED);
        Result<String> failure = ResultUtils.failure((Exception) bizException);
        Assertions.assertFalse(failure.isSuccess());
        Assertions.assertEquals(BizStatus.ACCOUNT_EXPIRED.getCode(), failure.getCode());
        Assertions.assertEquals(BizStatus.ACCOUNT_EXPIRED.getMessage(), failure.getMessage());
        Assertions.assertNull(failure.getData());
    }

    @Test
    void failureBizStatusData() {
        Result<String> failure = ResultUtils.failure(BizStatus.FORBIDDEN, "123");
        Assertions.assertFalse(failure.isSuccess());
        Assertions.assertEquals(BizStatus.FORBIDDEN.getCode(), failure.getCode());
        Assertions.assertEquals(BizStatus.FORBIDDEN.getMessage(), failure.getMessage());
        Assertions.assertNotNull(failure.getData());
    }

    @Test
    void failureBizStatus() {
        Result<String> failure = ResultUtils.failure(BizStatus.FORBIDDEN);
        Assertions.assertFalse(failure.isSuccess());
        Assertions.assertEquals(BizStatus.FORBIDDEN.getCode(), failure.getCode());
        Assertions.assertEquals(BizStatus.FORBIDDEN.getMessage(), failure.getMessage());
        Assertions.assertNull(failure.getData());
    }
}
