package com.etyero.service.test;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;

import com.alibaba.fastjson.JSON;
import com.etyero.entity.Code;
import com.etyero.service.CodeService;

/*@RunWith(SpringJUnit4ClassRunner.class) 
*/@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class CodeServiceTest {
	  private static Logger logger = LoggerFactory.getLogger(CodeServiceTest.class);
	    @Resource
	    private CodeService codeService;

	  //  @Test
	    public void test() {
	        List<Code> code = codeService.getCodeById(1);
	        logger.info("code----{}",code);
	        logger.info("code转为json");
	        logger.info(JSON.toJSONString(code));
	    }
}
