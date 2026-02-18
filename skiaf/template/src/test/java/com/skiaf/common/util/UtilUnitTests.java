/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.common.util;

import org.junit.Assert;
import org.junit.Test;

import com.skiaf.AbstractUnitTest;
import com.skiaf.core.util.Util;

public class UtilUnitTests extends AbstractUnitTest {

    @Test
    public void makeFormat() {
        String value = "1234512345";
        String format = "#####-#/####";
        String result = Util.makeFormat(value, format);

        Assert.assertEquals("12345-1/2345", result);
    }

    @Test
    public void removeChar() {
        String value = "1234512345";
        String target = "3";
        String result = Util.removeChar(value, target);

        Assert.assertEquals("12451245", result);
    }

    @Test
    public void isValidCtzNum() {
        String value = "640713-1018433";

        Assert.assertTrue(Util.isValidCtzNum(value));
    }

    @Test
    public void isValidCorpNum() {
        String value = "110111-0062432";

        Assert.assertTrue(Util.isValidCorpNum(value));
    }
}
