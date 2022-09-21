package com.example.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SpringbootApplicationTests {
    @Test
    void SwitchTypeTest(){
        Calculator calculator=new Calculator();
        assertEquals("Free",calculator.SwitchType("电动轮椅"));
        assertEquals("Free",calculator.SwitchType("电动代步工具"));
        assertEquals("Free",calculator.SwitchType("手动折叠轮椅"));
        assertEquals("Free",calculator.SwitchType("机械假肢及专用小型气瓶"));
        assertEquals("Free",calculator.SwitchType("心脏起搏器或其它植入人体的装置"));
        assertEquals("Free",calculator.SwitchType("便携式氧气浓缩器（POC）"));
        assertEquals("Free",calculator.SwitchType("持续正压呼吸机（CPAP）"));
        assertEquals("Free",calculator.SwitchType("其它内含锂电池的辅助设备"));
        assertEquals("Free",calculator.SwitchType("服务犬（包括：导盲犬、助听犬、辅助犬等）"));
        assertEquals("Sports1",calculator.SwitchType("高尔夫球包"));
        assertEquals("Sports1",calculator.SwitchType("保龄球"));
        assertEquals("Sports1",calculator.SwitchType("滑翔伞/降落伞"));
        assertEquals("Sports1",calculator.SwitchType("滑雪/滑水用具（不包括雪橇/水撬）"));
        assertEquals("Sports1",calculator.SwitchType("轮滑/滑板用具"));
        assertEquals("Sports1",calculator.SwitchType("潜水用具"));
        assertEquals("Sports1",calculator.SwitchType("射箭用具"));
        assertEquals("Sports1",calculator.SwitchType("曲棍球用具"));
        assertEquals("Sports1",calculator.SwitchType("冰球用具"));
        assertEquals("Sports1",calculator.SwitchType("网球用具"));
        assertEquals("Sports1",calculator.SwitchType("登山用具"));
        assertEquals("Sports1",calculator.SwitchType("自行车"));
        assertEquals("Sports2",calculator.SwitchType("皮划艇/独木舟"));
        assertEquals("Sports2",calculator.SwitchType("悬挂式滑翔运动用具"));
        assertEquals("Sports2",calculator.SwitchType("雪橇/水撬"));
        assertEquals("Sports2",calculator.SwitchType("冲浪板"));
        assertEquals("Sports2",calculator.SwitchType("风帆冲浪用具"));
        assertEquals("Sports2",calculator.SwitchType("橡皮艇或船"));
        assertEquals("Sports3",calculator.SwitchType("撑杆"));
        assertEquals("Sports3",calculator.SwitchType("标枪"));
        assertEquals("Sports3",calculator.SwitchType("单独包装的划船用具或浆"));
        assertEquals("Sports3",calculator.SwitchType("骑马用具"));
        assertEquals("Other1",calculator.SwitchType("睡袋"));
        assertEquals("Other1",calculator.SwitchType("背包"));
        assertEquals("Other1",calculator.SwitchType("野营用具"));
        assertEquals("Other1",calculator.SwitchType("渔具"));
        assertEquals("Other1",calculator.SwitchType("乐器"));
        assertEquals("Other1",calculator.SwitchType("辅助设备（非残疾、伤、病旅客托运）"));
        assertEquals("Other1",calculator.SwitchType("可折叠婴儿床"));
        assertEquals("Other2",calculator.SwitchType("小型电器或仪器"));
        assertEquals("Other2",calculator.SwitchType("媒体设备"));
        assertEquals("Other3",calculator.SwitchType("可作为行李运输的枪支"));
        assertEquals("Other4",calculator.SwitchType("可作为行李运输的子弹"));
        assertEquals("Other5",calculator.SwitchType("小动物（仅限家庭驯养的猫、狗）"));
        assertEquals("折叠式婴儿车或摇篮或婴儿汽车安全座椅",calculator.SwitchType("折叠式婴儿车或摇篮或婴儿汽车安全座椅"));
    }

    @Test
    void GetBaggageSystemTest(){
        Calculator calculator=new Calculator();
        assertEquals(BaggageSystem.Weight,calculator.GetBaggageSystem("中国境内（除港澳台）","中国境内（除港澳台）"));
        assertEquals(BaggageSystem.Piece1,calculator.GetBaggageSystem("中国境内（除港澳台）","美洲"));
        assertEquals(BaggageSystem.Piece1,calculator.GetBaggageSystem("美洲（除美国、加拿大外）", "中国境内（除港澳台）"));
        assertEquals(BaggageSystem.Piece2,calculator.GetBaggageSystem("美洲（除美国、加拿大外）", "欧洲"));
        assertEquals(BaggageSystem.Piece1,calculator.GetBaggageSystem("欧洲","中国境内（除港澳台）"));
        assertEquals(BaggageSystem.Piece2,calculator.GetBaggageSystem("欧洲","加拿大"));
        assertEquals(BaggageSystem.Piece1,calculator.GetBaggageSystem("中东","中国境内（除港澳台）"));
        assertEquals(BaggageSystem.Piece2,calculator.GetBaggageSystem("中东","加拿大"));
        assertEquals(BaggageSystem.Piece2,calculator.GetBaggageSystem("日本", "美洲（除美国、加拿大外）"));
        assertEquals(BaggageSystem.Piece1,calculator.GetBaggageSystem("日本", "中国境内（除港澳台）"));
        assertEquals(BaggageSystem.Piece1,calculator.GetBaggageSystem("夏威夷", "夏威夷"));
    }

    @Test
    void GetAreaTest(){
        Calculator calculator=new Calculator();
        assertEquals(Area.Area1,calculator.GetArea("美洲（除美国、加拿大外）","欧洲"));
        assertEquals(Area.Area3,calculator.GetArea("美洲（除美国、加拿大外）","加拿大"));
        assertEquals(Area.Area4,calculator.GetArea("美洲（除美国、加拿大外）","美国（除夏威夷）"));
        assertEquals(Area.Area5,calculator.GetArea("美洲（除美国、加拿大外）", "美洲（除美国、加拿大外）"));
        assertEquals(Area.Area1,calculator.GetArea("加勒比海地区","欧洲"));
        assertEquals(Area.Area3,calculator.GetArea("加勒比海地区","加拿大"));
        assertEquals(Area.Area4,calculator.GetArea("加勒比海地区","美国（除夏威夷）"));
        assertEquals(Area.Area5,calculator.GetArea("加勒比海地区","加勒比海地区"));
        assertEquals(Area.Area1,calculator.GetArea("欧洲", "美洲（除美国、加拿大外）"));
        assertEquals(Area.Area2,calculator.GetArea("欧洲", "非洲"));
        assertEquals(Area.Area3,calculator.GetArea("欧洲", "加拿大"));
        assertEquals(Area.Area4,calculator.GetArea("欧洲", "美国（除夏威夷）"));
        assertEquals(Area.Area5,calculator.GetArea("欧洲", "欧洲"));
        assertEquals(Area.Area1,calculator.GetArea("非洲","美洲（除美国、加拿大外）"));
        assertEquals(Area.Area2,calculator.GetArea("非洲","欧洲"));
        assertEquals(Area.Area3,calculator.GetArea("非洲","加拿大"));
        assertEquals(Area.Area4,calculator.GetArea("非洲","美国（除夏威夷）"));
        assertEquals(Area.Area5,calculator.GetArea("非洲","非洲"));
        assertEquals(Area.Area1,calculator.GetArea("中东","美洲（除美国、加拿大外）"));
        assertEquals(Area.Area2,calculator.GetArea("中东","日本"));
        assertEquals(Area.Area3,calculator.GetArea("中东","加拿大"));
        assertEquals(Area.Area4,calculator.GetArea("中东","美国（除夏威夷）"));
        assertEquals(Area.Area5,calculator.GetArea("中东","欧洲"));
        assertEquals(Area.Area1,calculator.GetArea("西南太平洋","美洲（除美国、加拿大外）"));
        assertEquals(Area.Area2,calculator.GetArea("西南太平洋","欧洲"));
        assertEquals(Area.Area3,calculator.GetArea("西南太平洋","加拿大"));
        assertEquals(Area.Area4,calculator.GetArea("西南太平洋","美国（除夏威夷）"));
        assertEquals(Area.Area5,calculator.GetArea("西南太平洋","中东"));
        assertEquals(Area.Area1,calculator.GetArea("日本","美洲（除美国、加拿大外）"));
        assertEquals(Area.Area2,calculator.GetArea("日本","欧洲"));
        assertEquals(Area.Area3,calculator.GetArea("日本","加拿大"));
        assertEquals(Area.Area4,calculator.GetArea("日本","美国（除夏威夷）"));
        assertEquals(Area.Area5,calculator.GetArea("日本","加勒比海地区"));
        assertEquals(Area.Area5,calculator.GetArea("加拿大","加拿大"));
        assertEquals(Area.Area4,calculator.GetArea("加拿大","美国（除夏威夷）"));
        assertEquals(Area.Area3,calculator.GetArea("加拿大","欧洲"));
        assertEquals(Area.Area5,calculator.GetArea("美国（除夏威夷）","美国（除夏威夷）"));
        assertEquals(Area.Area4,calculator.GetArea("美国（除夏威夷）","欧洲"));
        assertEquals(Area.Area5,calculator.GetArea("其他","其他"));
    }
}
