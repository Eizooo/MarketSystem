import com.ssm.controller.WeatherController;
import org.junit.Test;

/**
 * @author Eizooo
 * @date 2021/5/13 18:15
 */
public class Weather_Test {
    @Test
    public void test01(){
        WeatherController weatherController = new WeatherController();
        weatherController.queryWeather("杭州",null);
    }
}
