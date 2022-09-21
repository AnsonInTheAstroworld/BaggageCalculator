package com.example.springboot;

import com.example.springboot.entity.Baggage;
import com.example.springboot.entity.SpecialBaggage;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

enum BaggageSystem{
    Weight,Piece1,Piece2
}
enum Area{
    Area1,Area2,Area3,Area4,Area5
}

@RestController
@RequestMapping("/calculator")
public class Calculator {
    HashMap<Integer,Baggage> BaggageList;
    HashMap<Integer,SpecialBaggage> SpecialBaggageList;

    @PostMapping("/load")
    public void Load(){
        BaggageList=new HashMap<>();
        SpecialBaggageList=new HashMap<>();
    }

    @PostMapping
    public Result<?> SaveBaggage(@RequestBody Baggage baggage){
        BaggageList.put(baggage.getIndex(),baggage);
        return Result.success();
    }

    @DeleteMapping("/{index}")
    public Result<?> DeleteBaggage(@PathVariable Integer index){
        BaggageList.remove(index);
        return Result.success();
    }

    public String SwitchType(String type){
        switch (type){
            case "电动轮椅":
            case "电动代步工具":
            case "手动折叠轮椅":
            case "机械假肢及专用小型气瓶":
            case "心脏起搏器或其它植入人体的装置":
            case "便携式氧气浓缩器（POC）":
            case "持续正压呼吸机（CPAP）":
            case "其它内含锂电池的辅助设备":
            case "服务犬（包括：导盲犬、助听犬、辅助犬等）":
                return "Free";
            case "高尔夫球包":
            case "保龄球":
            case "滑翔伞/降落伞":
            case "滑雪/滑水用具（不包括雪橇/水撬）":
            case "轮滑/滑板用具":
            case "潜水用具":
            case "射箭用具":
            case "曲棍球用具":
            case "冰球用具":
            case "网球用具":
            case "登山用具":
            case "自行车":
                return "Sports1";
            case "皮划艇/独木舟":
            case "悬挂式滑翔运动用具":
            case "雪橇/水撬":
            case "冲浪板":
            case "风帆冲浪用具":
            case "橡皮艇或船":
                return "Sports2";
            case "撑杆":
            case "标枪":
            case "单独包装的划船用具或浆":
            case "骑马用具":
                return "Sports3";
            case "睡袋":
            case "背包":
            case "野营用具":
            case "渔具":
            case "乐器":
            case "辅助设备（非残疾、伤、病旅客托运）":
            case "可折叠婴儿床":
                return "Other1";
            case "小型电器或仪器":
            case "媒体设备":
                return "Other2";
            case "可作为行李运输的枪支":
                return "Other3";
            case "可作为行李运输的子弹":
                return "Other4";
            case "小动物（仅限家庭驯养的猫、狗）":
                return "Other5";
            default:
                return type;
        }
    }

    @PostMapping("/special")
    public Result<?> SaveSpecialBaggage(@RequestBody SpecialBaggage baggage){
        baggage.setType(SwitchType(baggage.getType()));
        SpecialBaggageList.put(baggage.getIndex(),baggage);
        return Result.success(baggage.getType());
    }

    @DeleteMapping("/special/{index}")
    public Result<?> DeleteSpecialBaggage(@PathVariable Integer index){
        SpecialBaggageList.remove(index);
        return Result.success();
    }

    @GetMapping
    public Result<?> Calculate(@RequestParam String start,
                           @RequestParam String destination,
                           @RequestParam Double price,
                           @RequestParam String ticket,
                           @RequestParam String cabin,
                           @RequestParam String membership
                           ){
        BaggageSystem baggageSystem=GetBaggageSystem(start,destination);
        if(baggageSystem==BaggageSystem.Weight){
            double maxWeight=0.0;int hasBaby=0;
            switch (ticket){
                case "成人/儿童客票":
                    switch (cabin){
                        case "头等舱":
                            maxWeight=40.0;break;
                        case "公务舱":
                            maxWeight=30.0;break;
                        case "悦享经济舱、超级经济舱":
                        case "经济舱":
                            maxWeight=20.0;break;
                    }
                    break;
                case "婴儿客票":
                    maxWeight=10.0;hasBaby=1;
            }
            switch (membership){
                case "凤凰知音终身白金卡、白金卡":
                    maxWeight+=30.0;break;
                case "凤凰知音金卡、银卡":
                case "星空联盟金卡":
                    maxWeight+=20.0;
            }
            return CalWeight(maxWeight,price,hasBaby);
        }
        else {
            Area area=GetArea(start,destination);
            int freeBigBagNum=0,freeSmallBagNum=0,hasBaby=0;
            boolean isHead=false;
            switch (ticket){
                case "成人/儿童客票":
                    switch (cabin){
                        case "头等舱": case "公务舱":
                            freeBigBagNum=2;break;
                        case "悦享经济舱、超级经济舱":
                            freeSmallBagNum=2;break;
                        case "经济舱":
                            if(baggageSystem==BaggageSystem.Piece1)
                                freeSmallBagNum=1;
                            else freeSmallBagNum=2;
                            break;
                    }
                    break;
                case "婴儿客票":
                    freeSmallBagNum=1;hasBaby=1;break;
            }
            switch (membership){
                case "凤凰知音终身白金卡、白金卡": case "凤凰知音金卡、银卡":
                    switch (cabin){
                        case "头等舱": case "公务舱":
                            freeBigBagNum+=1;isHead=true;break;
                        default:
                            freeSmallBagNum+=1;break;
                    }
                    break;
                case "星空联盟金卡":
                    freeSmallBagNum+=1;break;
            }
            return CalPiece(area,freeBigBagNum,freeSmallBagNum,isHead,hasBaby);
        }
    }

    public BaggageSystem GetBaggageSystem(String start,String destination){
        switch (start){
            case "中国境内（除港澳台）":
                if ("中国境内（除港澳台）".equals(destination)) {
                    return BaggageSystem.Weight;
                }
                return BaggageSystem.Piece1;
            case "美洲（除美国、加拿大外）":
            case "美国（除夏威夷）":
            case "加拿大":
            case "加勒比海地区":
                switch (destination){
                    case "中国境内（除港澳台）":
                    case "夏威夷":
                    case "美洲（除美国、加拿大外）":
                    case "美国（除夏威夷）":
                    case "加拿大":
                    case "其他":
                        return BaggageSystem.Piece1;
                    default:
                        return BaggageSystem.Piece2;
                }
            case "欧洲":
            case "非洲":
            case "亚洲（除日本、巴基斯坦、新加坡、哈萨克斯坦）":
                switch (destination){
                    case "中国境内（除港澳台）":
                    case "夏威夷":
                    case "欧洲":
                    case "非洲":
                    case "亚洲（除日本、巴基斯坦、新加坡、哈萨克斯坦）":
                    case "其他":
                        return BaggageSystem.Piece1;
                    default:
                        return BaggageSystem.Piece2;
                }
            case "中东":
                switch (destination){
                    case "中国境内（除港澳台）":
                    case "夏威夷":
                    case "中东":
                    case "其他":
                        return BaggageSystem.Piece1;
                    default:
                        return BaggageSystem.Piece2;
                }
            case "日本":
            case "巴基斯坦":
            case "新加坡":
            case "哈萨克斯坦":
            case "西南太平洋":
                switch (destination){
                    case "美洲（除美国、加拿大外）":
                    case "美国（除夏威夷）":
                    case "加拿大":
                    case "加勒比海地区":
                    case "欧洲":
                    case "非洲":
                    case "中东":
                    case "亚洲（除日本、巴基斯坦、新加坡、哈萨克斯坦）":
                        return BaggageSystem.Piece2;
                    default:
                        return BaggageSystem.Piece1;
                }
            case "夏威夷":
            default:
                return BaggageSystem.Piece1;
        }
    }

    public Area GetArea(String start,String destination){
        switch (start){
            case "美洲（除美国、加拿大外）":
                switch (destination){
                    case "欧洲":
                    case "非洲":
                    case "中东":
                    case "中国境内（除港澳台）":
                    case "亚洲（除日本、巴基斯坦、新加坡、哈萨克斯坦）":
                    case "日本":
                    case "巴基斯坦":
                    case "新加坡":
                    case "哈萨克斯坦":
                    case "西南太平洋":
                        return Area.Area1;
                    case "加拿大":
                        return Area.Area3;
                    case "美国（除夏威夷）":
                    case "夏威夷":
                        return Area.Area4;
                    default:
                        return Area.Area5;
                }
            case "加勒比海地区":
                switch (destination){
                    case "欧洲":
                    case "非洲":
                    case "中东":
                    case "亚洲（除日本、巴基斯坦、新加坡、哈萨克斯坦）":
                    case "中国境内（除港澳台）":
                    case "巴基斯坦":
                    case "新加坡":
                    case "哈萨克斯坦":
                    case "西南太平洋":
                        return Area.Area1;
                    case "加拿大":
                        return Area.Area3;
                    case "美国（除夏威夷）":
                    case "夏威夷":
                        return Area.Area4;
                    default:
                        return Area.Area5;
                }
            case "欧洲":
                switch (destination){
                    case "美洲（除美国、加拿大外）":
                    case "加勒比海地区":
                        return  Area.Area1;
                    case "非洲":
                    case "亚洲（除日本、巴基斯坦、新加坡、哈萨克斯坦）":
                    case "中国境内（除港澳台）":
                    case "巴基斯坦":
                    case "新加坡":
                    case "哈萨克斯坦":
                    case "西南太平洋":
                    case "日本":
                        return Area.Area2;
                    case "加拿大":
                        return Area.Area3;
                    case "美国（除夏威夷）":
                    case "夏威夷":
                        return Area.Area4;
                    default:
                        return Area.Area5;
                }
            case "非洲":
            case "亚洲（除日本、巴基斯坦、新加坡、哈萨克斯坦）":
            case "中国境内（除港澳台）":
            case "巴基斯坦":
            case "新加坡":
            case "哈萨克斯坦":
                switch (destination){
                    case "美洲（除美国、加拿大外）":
                    case "加勒比海地区":
                        return Area.Area1;
                    case "欧洲":
                    case "西南太平洋":
                    case "日本":
                        return Area.Area2;
                    case "加拿大":
                        return Area.Area3;
                    case "美国（除夏威夷）":
                    case "夏威夷":
                        return Area.Area4;
                    default:
                        return Area.Area5;
                }
            case "中东":
                switch (destination){
                    case "美洲（除美国、加拿大外）":
                    case "加勒比海地区":
                        return Area.Area1;
                    case "日本":
                        return Area.Area2;
                    case "加拿大":
                        return Area.Area3;
                    case "美国（除夏威夷）":
                    case "夏威夷":
                        return Area.Area4;
                    default:
                        return Area.Area5;
                }
            case "西南太平洋":
                switch (destination){
                    case "美洲（除美国、加拿大外）":
                    case "加勒比海地区":
                        return Area.Area1;
                    case "欧洲":
                    case "非洲":
                    case "亚洲（除日本、巴基斯坦、新加坡、哈萨克斯坦）":
                    case "中国境内（除港澳台）":
                    case "巴基斯坦":
                    case "新加坡":
                    case "哈萨克斯坦":
                    case "日本":
                        return Area.Area2;
                    case "加拿大":
                        return Area.Area3;
                    case "美国（除夏威夷）":
                    case "夏威夷":
                        return Area.Area4;
                    default:
                        return Area.Area5;
                }
            case "日本":
                switch (destination){
                    case "美洲（除美国、加拿大外）":
                        return Area.Area1;
                    case "欧洲":
                    case "非洲":
                    case "中东":
                    case "亚洲（除日本、巴基斯坦、新加坡、哈萨克斯坦）":
                    case "中国境内（除港澳台）":
                    case "巴基斯坦":
                    case "新加坡":
                    case "哈萨克斯坦":
                    case "西南太平洋":
                        return Area.Area2;
                    case "加拿大":
                        return Area.Area3;
                    case "美国（除夏威夷）":
                    case "夏威夷":
                        return Area.Area4;
                    default:
                        return Area.Area5;
                }
            case "加拿大":
                switch (destination){
                    case "加拿大":
                        return Area.Area5;
                    case "美国（除夏威夷）":
                    case "夏威夷":
                        return Area.Area4;
                    default:
                        return Area.Area3;
                }
            case "美国（除夏威夷）":
            case "夏威夷":
                switch (destination){
                    case "美国（除夏威夷）":
                    case "夏威夷":
                        return Area.Area5;
                    default:
                        return Area.Area4;
                }
            default:
                return Area.Area5;
        }
    }

    private Result<?> CalWeight(Double maxWeight,Double price,Integer hasBaby){
        Double totalWeight=0.0;
        for(Map.Entry<Integer,Baggage> entry:BaggageList.entrySet()){
            totalWeight+=entry.getValue().getWeight();
        }
        Double extra=0.0;
        for(Map.Entry<Integer,SpecialBaggage> entry:SpecialBaggageList.entrySet()){
            switch (entry.getValue().getType()){
                case "Sports1":
                case "Other1":
                    totalWeight+=entry.getValue().getWeight();break;
                case "Sports2":
                case "Sports3":
                case "Other2":
                case "Other3":
                case "Other4":
                case "Other5":
                    extra+=entry.getValue().getWeight();break;
                case "折叠式婴儿车或摇篮或婴儿汽车安全座椅":
                    if(hasBaby>0) hasBaby--;
                    else extra+=entry.getValue().getWeight();
            }
        }
        Double surplus=totalWeight>maxWeight?totalWeight-maxWeight:0;
        return Result.success((extra + surplus) * price * 0.015);
    }

    private Result<?> CalPiece(Area area,int freeBigBagNum,int freeSmallBagNum,boolean isHead,int hasBaby){
        int surplus=0;
        double fee=0.0;
        switch (area){
            case Area1:
                for(Map.Entry<Integer,Baggage> entry:BaggageList.entrySet()){
                    if(entry.getValue().getWeight()<23){ //不超重量
                        if(freeSmallBagNum>0) freeSmallBagNum--;
                        else if(freeBigBagNum>0) freeBigBagNum--;
                        else surplus++;
                        if(entry.getValue().getSize()>158) //超尺寸
                            fee+=980;
                    }
                    else if(entry.getValue().getWeight()>23){ //超重量
                        if(freeBigBagNum>0) {
                            freeBigBagNum--;
                        }
                        else if(freeSmallBagNum>0){
                            freeSmallBagNum--;
                            if(entry.getValue().getSize()<=158){ //不超尺寸
                                if(entry.getValue().getWeight()<=28) fee+=380;
                                else fee+=980;
                            }
                            else fee+=1400;
                            continue;
                        }
                        else surplus++;
                        if(entry.getValue().getSize()<=158){ //不超尺寸
                            if(!isHead) {
                                if(entry.getValue().getWeight()<=28) fee+=380;
                                else fee+=980;
                            }
                        }
                        else { //超尺寸
                            if (isHead) fee+=980;
                            else fee+=1400;
                        }
                    }
                }
                break;

            case Area2:
                for(Map.Entry<Integer,Baggage> entry:BaggageList.entrySet()){
                    if(entry.getValue().getWeight()<23){ //不超重量
                        if(freeSmallBagNum>0) freeSmallBagNum--;
                        else if(freeBigBagNum>0) freeBigBagNum--;
                        else surplus++;
                        if(entry.getValue().getSize()>158) //超尺寸
                            fee+=690;
                    }
                    else if(entry.getValue().getWeight()>23){ //超重量
                        if(freeBigBagNum>0) {
                            freeBigBagNum--;
                        }
                        else if(freeSmallBagNum>0){
                            freeSmallBagNum--;
                            if(entry.getValue().getSize()<=158){ //不超尺寸
                                if(entry.getValue().getWeight()<=28) fee+=280;
                                else fee+=690;
                            }
                            else fee+=1100;
                            continue;
                        }
                        else surplus++;
                        if(entry.getValue().getSize()<=158){ //不超尺寸
                            if(!isHead) {
                                if(entry.getValue().getWeight()<=28) fee+=280;
                                else fee+=690;
                            }
                        }
                        else { //超尺寸
                            if (isHead) fee+=690;
                            else fee+=1100;
                        }
                    }
                }
                break;

            case Area3:
                for(Map.Entry<Integer,Baggage> entry:BaggageList.entrySet()){
                    if(entry.getValue().getWeight()<23){ //不超重量
                        if(freeSmallBagNum>0) freeSmallBagNum--;
                        else if(freeBigBagNum>0) freeBigBagNum--;
                        else surplus++;
                        if(entry.getValue().getSize()>158) //超尺寸
                            fee+=520;
                    }
                    else if(entry.getValue().getWeight()>23){ //超重量
                        if(freeBigBagNum>0) {
                            freeBigBagNum--;
                        }
                        else if(freeSmallBagNum>0){
                            freeSmallBagNum--;
                            fee+=520;
                            continue;
                        }
                        else surplus++;
                        if(entry.getValue().getSize()<=158) //不超尺寸
                            if(!isHead) fee+=520;
                        else fee+=520;
                    }
                }
                break;

            case Area4:
                for(Map.Entry<Integer,Baggage> entry:BaggageList.entrySet()){
                    if(entry.getValue().getWeight()<23){ //不超重量
                        if(freeSmallBagNum>0) freeSmallBagNum--;
                        else if(freeBigBagNum>0) freeBigBagNum--;
                        else surplus++;
                        if(entry.getValue().getSize()>158) //超尺寸
                            fee+=1040;
                    }
                    else if(entry.getValue().getWeight()>23){ //超重量
                        if(freeBigBagNum>0) {
                            freeBigBagNum--;
                        }
                        else if(freeSmallBagNum>0){
                            freeSmallBagNum--;
                            if(entry.getValue().getSize()<=158){ //不超尺寸
                                if(entry.getValue().getWeight()<=28) fee+=690;
                                else fee+=1040;
                            }
                            else fee+=2050;
                            continue;
                        }
                        else surplus++;
                        if(entry.getValue().getSize()<=158){ //不超尺寸
                            if(!isHead) {
                                if(entry.getValue().getWeight()<=28) fee+=690;
                                else fee+=1040;
                            }
                        }
                        else { //超尺寸
                            if (isHead) fee+=1040;
                            else fee+=2050;
                        }
                    }
                }
                break;

            case Area5:
                for(Map.Entry<Integer,Baggage> entry:BaggageList.entrySet()){
                    if(entry.getValue().getWeight()<23){ //不超重量
                        if(freeSmallBagNum>0) freeSmallBagNum--;
                        else if(freeBigBagNum>0) freeBigBagNum--;
                        else surplus++;
                        if(entry.getValue().getSize()>158) //超尺寸
                            fee+=520;
                    }
                    else if(entry.getValue().getWeight()>23){ //超重量
                        if(freeBigBagNum>0) {
                            freeBigBagNum--;
                        }
                        else if(freeSmallBagNum>0){
                            freeSmallBagNum--;
                            if(entry.getValue().getSize()<=158){ //不超尺寸
                                if(entry.getValue().getWeight()<=28) fee+=210;
                                else fee+=520;
                            }
                            else fee+=830;
                            continue;
                        }
                        else surplus++;
                        if(entry.getValue().getSize()<=158){ //不超尺寸
                            if(!isHead) {
                                if(entry.getValue().getWeight()<=28) fee+=210;
                                else fee+=520;
                            }
                        }
                        else { //超尺寸
                            if (isHead) fee+=520;
                            else fee+=830;
                        }
                    }
                }
                break;
        }

        for(Map.Entry<Integer,SpecialBaggage> entry:SpecialBaggageList.entrySet()){
            switch (entry.getValue().getType()){
                case "折叠式婴儿车或摇篮或婴儿汽车安全座椅":
                    if(hasBaby>0){
                        hasBaby--;break;
                    }
                case "Sports1":
                case "Other1":
                    switch (area){
                        case Area1:
                            if(entry.getValue().getWeight()<23){ //不超重量
                                if(freeSmallBagNum>0) freeSmallBagNum--;
                                else if(freeBigBagNum>0) freeBigBagNum--;
                                else surplus++;
                            }
                            else if(entry.getValue().getWeight()>23){ //超重量
                                if(freeBigBagNum>0) {
                                    freeBigBagNum--;
                                }
                                else if(freeSmallBagNum>0){
                                    freeSmallBagNum--;
                                    if(entry.getValue().getWeight()<=28) fee+=380;
                                    else fee+=980;
                                    continue;
                                }
                                else surplus++;
                                if(!isHead) {
                                    if(entry.getValue().getWeight()<=28) fee+=380;
                                    else fee+=980;
                                }
                            }
                            break;
                        case Area2:
                            if(entry.getValue().getWeight()<23){ //不超重量
                                if(freeSmallBagNum>0) freeSmallBagNum--;
                                else if(freeBigBagNum>0) freeBigBagNum--;
                                else surplus++;
                            }
                            else if(entry.getValue().getWeight()>23){ //超重量
                                if(freeBigBagNum>0) {
                                    freeBigBagNum--;
                                }
                                else if(freeSmallBagNum>0){
                                    freeSmallBagNum--;
                                    if(entry.getValue().getWeight()<=28) fee+=280;
                                    else fee+=690;
                                    continue;
                                }
                                else surplus++;
                                if(!isHead) {
                                    if(entry.getValue().getWeight()<=28) fee+=280;
                                    else fee+=690;
                                }
                            }
                            break;
                        case Area3:
                            if(entry.getValue().getWeight()<23){ //不超重量
                                if(freeSmallBagNum>0) freeSmallBagNum--;
                                else if(freeBigBagNum>0) freeBigBagNum--;
                                else surplus++;
                            }
                            else if(entry.getValue().getWeight()>23){ //超重量
                                if(freeBigBagNum>0) {
                                    freeBigBagNum--;
                                }
                                else if(freeSmallBagNum>0){
                                    freeSmallBagNum--;
                                    fee+=580;
                                    continue;
                                }
                                else surplus++;
                                if(!isHead) fee+=580;
                            }
                            break;
                        case Area4:
                            if(entry.getValue().getWeight()<23){ //不超重量
                                if(freeSmallBagNum>0) freeSmallBagNum--;
                                else if(freeBigBagNum>0) freeBigBagNum--;
                                else surplus++;
                            }
                            else if(entry.getValue().getWeight()>23){ //超重量
                                if(freeBigBagNum>0) {
                                    freeBigBagNum--;
                                }
                                else if(freeSmallBagNum>0){
                                    freeSmallBagNum--;
                                    if(entry.getValue().getWeight()<=28) fee+=690;
                                    else fee+=1040;
                                    continue;
                                }
                                else surplus++;
                                if(!isHead) {
                                    if(entry.getValue().getWeight()<=28) fee+=690;
                                    else fee+=1040;
                                }
                            }
                            break;
                        case Area5:
                            if(entry.getValue().getWeight()<23){ //不超重量
                                if(freeSmallBagNum>0) freeSmallBagNum--;
                                else if(freeBigBagNum>0) freeBigBagNum--;
                                else surplus++;
                            }
                            else if(entry.getValue().getWeight()>23){ //超重量
                                if(freeBigBagNum>0) {
                                    freeBigBagNum--;
                                }
                                else if(freeSmallBagNum>0){
                                    freeSmallBagNum--;
                                    if(entry.getValue().getWeight()<=28) fee+=210;
                                    else fee+=520;
                                    continue;
                                }
                                else surplus++;
                                if(!isHead) {
                                    if(entry.getValue().getWeight()<=28) fee+=210;
                                    else fee+=520;
                                }
                            }
                            break;
                    }
                    break;
                case "Sports2":
                    if(entry.getValue().getWeight()<23) fee+=2600;
                    else if(entry.getValue().getWeight()<28) fee+=3900;
                    else fee+=5200;
                    break;
                case "Sports3":
                    if(entry.getValue().getWeight()<23) fee+=1300;
                    else if(entry.getValue().getWeight()<28) fee+=2600;
                    else fee+=3900;
                    break;
                case "Other2":
                    if(entry.getValue().getWeight()<23) fee+=490;
                    else fee+=3900;
                    break;
                case "Other3":
                    if(entry.getValue().getWeight()<23) fee+=1300;
                    else fee+=2600;
                    break;
                case "Other4":
                    fee+=1300;break;
                case "Other5":
                    if(entry.getValue().getWeight()<23) fee+=3900;
                    else if(entry.getValue().getWeight()<28) fee+=5200;
                    else fee+=7800;
                    break;
            }
        }

        switch (area){
            case Area1:
                switch (surplus){
                    case 0:break;
                    case 1:fee+=1400;break;
                    case 2:fee+=3400;break;
                    default:fee+=3400+(surplus-2)*3000;
                }
                break;
            case Area2:
                switch (surplus){
                    case 0:break;
                    case 1:fee+=1100;break;
                    case 2:fee+=2200;break;
                    default:fee+=2200+(surplus-2)*1590;
                }
                break;
            case Area3:
                switch (surplus){
                    case 0:break;
                    case 1:fee+=1170;break;
                    case 2:fee+=2340;break;
                    default:fee+=2340+(surplus-2)*1590;
                }
                break;
            case Area4:
                switch (surplus){
                    case 0:break;
                    case 1:fee+=1380;break;
                    case 2:fee+=2760;break;
                    default:fee+=2760+(surplus-2)*1590;
                }
                break;
            case Area5:
                switch (surplus){
                    case 0:break;
                    case 1:fee+=830;break;
                    case 2:fee+=1930;break;
                    default:fee+=1930+(surplus-2)*1590;
                }
                break;
        }

        return Result.success(fee);
    }
}
