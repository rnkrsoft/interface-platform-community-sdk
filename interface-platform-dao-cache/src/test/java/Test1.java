import com.rnkrsoft.framework.orm.jdbc.NameMode;
import com.rnkrsoft.framework.orm.jdbc.WordMode;
import com.rnkrsoft.framework.orm.jdbc.mysql.DataEngineType;
import com.rnkrsoft.framework.orm.untils.SqlScriptUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Administrator on 2018/6/21.
 */
public class Test1 {
    @Test
    public void test2() throws IOException {
        SqlScriptUtils.generate("./target/sql.sql", NameMode.customize, "", NameMode.entity, "", NameMode.entity, "", DataEngineType.AUTO, WordMode.lowerCase, WordMode.lowerCase, true, true, "com.rnkrsoft.platform.jdbc");
    }
}
