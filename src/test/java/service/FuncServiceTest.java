package service;

import act.Act;
import base.TestBase;
import com.alibaba.fastjson.JSON;
import com.artlongs.sys.dao.SysFuncDao;
import com.artlongs.sys.model.SysFunc;
import com.artlongs.sys.service.SysFuncService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.osgl.http.H;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;

/**
 * Function:
 *
 * @Autor: leeton
 * @Date : 1/17/18
 */
public class FuncServiceTest extends TestBase {

    private SysFuncService sysFuncService;


    @Before
    public void prepare() throws Exception {
        Act.start("act-eagle");
        sysFuncService = new SysFuncService(new SysFuncDao());
    }

    @Test
    public void testTopMenuOfTree(){
        Map<Long, List<SysFunc>> moduleMap = sysFuncService.getModuleTreeMap();
        SysFunc topMenu = sysFuncService.buildFuncTree(moduleMap);
        Object json = JSON.toJSON(topMenu);
        System.err.println("topmenu = " + json);

    }
}
