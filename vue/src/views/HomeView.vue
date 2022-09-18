<template xmlns="http://www.w3.org/1999/html">
  <!--header-->
  <div style="height:100px;line-height: 50px;border-bottom: 1px solid #cccccc;background-color:dodgerblue" >
    <div style="padding-left: 30px;padding-top:20px;font-weight: bold;color: whitesmoke;font-size: xx-large;">中国国际航空公司</div>
  </div>

  <div style="padding: 20px;background-color:aliceblue">
    <!--计算器-->
    <div style="padding-left: 20px">
      <span style="font-size: xx-large;font-weight: lighter">行李计算器</span>
    </div>
    <div style="padding-top: 10px">
      <el-card class="box-card" shadow="hover" style="background-color: white">
        <!--航班线路-->
        <div>
          <div class="card-header">
            <span style="font-size: x-large;font-weight: lighter">航班路线</span>
          </div>
          <div style="padding-top: 20px">
            <span>从</span>
            <el-select v-model="start" placeholder=" " filterable style="padding-left: 10px;width: 300px">
              <el-option
                  v-for="item in Locations"
                  :key="item.label"
                  :label="item.label"
                  :value="item.label"
              />
            </el-select>
            <span style="padding-left: 20px">到</span>
            <el-select v-model="destination" placeholder=" " filterable style="padding-left: 10px;width: 300px">
              <el-option
                  v-for="item in Locations"
                  :key="item.label"
                  :label="item.label"
                  :value="item.label"
              />
            </el-select>
            <span style="padding-left: 20px">经济舱单程票价</span>
            <span style="padding-left: 10px;width: 300px">
              <el-input-number :min="0" v-model="price" />
            </span>
          </div>
        </div>

        <!--机票类别-->
        <div>
          <div class="card-header" style="padding-top: 30px" >
            <span style="font-size: x-large;font-weight: lighter">机票类别</span>
          </div>
          <div style="padding-top: 20px">
            <span>客票类型</span>
            <el-select v-model="ticket" filterable placeholder=" " style="padding-left: 10px;width: 300px">
              <el-option
                  v-for="item in TicketTypes"
                  :key="item.label"
                  :label="item.label"
                  :value="item.label"
              />
            </el-select>
            <span style="padding-left: 20px">客舱类型</span>
            <el-select v-model="cabin" filterable placeholder=" " style="padding-left: 10px;width: 300px">
              <el-option
                  v-for="item in CabinTypes"
                  :key="item.label"
                  :label="item.label"
                  :value="item.label"
              />
            </el-select>
            <span style="padding-left: 20px">会员类型</span>
            <el-select v-model="membership" filterable placeholder=" " style="padding-left: 10px;width: 300px">
              <el-option
                  v-for="item in MembershipTypes"
                  :key="item.label"
                  :label="item.label"
                  :value="item.label"
              />
            </el-select>
          </div>
        </div>

        <!--行李状况-->
        <div>
          <div class="card-header" style="padding-top: 30px" >
            <span style="font-size: x-large;font-weight: lighter">行李状况</span>
          </div>
          <div style="padding-top: 20px">
            <el-table :data="BaggageTable" style="width: 60%">
              <el-table-column label="尺寸">
                <template #default="scope">
                  <el-input-number placeholder="长度" :min="60" :max=203 v-show="scope.row.edit" v-model="scope.row.size"/>
                  <span  v-show="!scope.row.edit">{{scope.row.size}}</span>
                </template>
              </el-table-column>
              <el-table-column label="重量">
                <template #default="scope">
                  <el-input-number placeholder="重量" :min="2" :max="32" v-show="scope.row.edit" v-model="scope.row.weight"/>
                  <span  v-show="!scope.row.edit">{{scope.row.weight}}</span>
                </template>
              </el-table-column>
              <el-table-column fixed="right" width="150px">
                <template #default="scope">
                  <el-button link v-show="!scope.row.edit" @click="scope.row.edit=true">
                    编辑
                  </el-button>
                  <el-button link v-show="scope.row.edit" @click="SaveBaggage(scope.row)">
                    保存
                  </el-button>
                  <el-button link  style="color: darkgoldenrod" @click="DeleteBaggage(scope.row)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-button class="mt-4" style="width: 60%;color: dodgerblue" @click="AddBaggage">
              添加行李
            </el-button>
          </div>
        </div>

        <!--特殊行李-->
        <div>
          <div class="card-header" style="padding-top: 30px" >
            <span style="font-size: x-large;font-weight: lighter">特殊行李</span>
          </div>
          <div style="padding-top: 20px">
            <el-table :data="SpecialBaggageTable" style="width: 60%">
              <el-table-column label="类型" width="500">
                <template #default="scope">
                  <el-select v-model="scope.row.type" filterable placeholder="特殊行李类型" v-show="scope.row.edit" style="width: 100%">
                    <el-option
                        v-for="item in SpecialBaggageTypes"
                        :key="item.label"
                        :label="item.label"
                        :value="item.label"
                    />
                  </el-select>
                  <span  v-show="!scope.row.edit">{{scope.row.type}}</span>
                </template>
              </el-table-column>
              <el-table-column label="重量" >
                <template #default="scope">
                  <el-input-number placeholder="重量" :min="2" :max="32" v-show="scope.row.edit" v-model="scope.row.weight"/>
                  <span  v-show="!scope.row.edit">{{scope.row.weight}}</span>
                </template>
              </el-table-column>
              <el-table-column fixed="right" width="150px">
                <template #default="scope">
                  <el-button link v-show="!scope.row.edit" @click="scope.row.edit=true">
                    编辑
                  </el-button>
                  <el-button link v-show="scope.row.edit" @click="SaveSpecialBaggage(scope.row)">
                    保存
                  </el-button>
                  <el-button link  style="color: darkgoldenrod" @click="DeleteSpecialBaggage(scope.row)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-button class="mt-4" style="width: 60%;color: dodgerblue" @click="AddSpecialBaggage">
              添加特殊行李
            </el-button>
          </div>
        </div>

        <div style="padding-top: 20px">
          <el-row>
            <el-button style="width: 150px;height: 50px;font-size: larger;background-color: cornflowerblue;color: white" @click="Calculate">
              计算行李
            </el-button>
            <el-input v-model="result" style="width: 200px;padding-left: 20px;font-size: larger" readonly="True"/>
          </el-row>
        </div>
      </el-card>
    </div>


    <!--更多详情-->
    <div style="padding-top: 20px;padding-left: 20px">
      <span style="font-size: xx-large;font-weight: lighter">更多详情</span>
    </div>
    <div style="padding-top: 10px;padding-bottom:20px;flex-direction: column">
      <el-row>
        <el-col :span="8">
          <el-card class="box-card" shadow="hover" style="background-color: white">
            <template #header>
              <div class="card-header">
                <span style="font-size: x-large;font-weight: lighter">随身携带行李</span>
              </div>
            </template>
            <div>
              <span>在此处查看您可以随身携带哪些行李进入乘客舱。</span>
            </div>
            <div class="bottom" style="padding-top: 10px">
              <el-button type="text" style="font-weight: bold;color: cornflowerblue" @click="AccompaniedBaggageDetailVisible=true">
                查看随身行李
              </el-button>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8" style="padding-left: 10px">
          <el-card class="box-card" shadow="hover" style="background-color: white">
            <template #header>
              <div class="card-header">
                <span style="font-size: x-large;font-weight: lighter">行李托运限制</span>
              </div>
            </template>
            <div>
              <span>您可在此查看我们对于托运行李的相关规定，并提前做好准备。</span>
            </div>
            <div class="bottom" style="padding-top: 10px">
              <el-button type="text" style="font-weight: bold;color: cornflowerblue" @click="ConditionDetailVisible=true">
                行李托运限制
              </el-button>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8" style="padding-left: 10px">
          <el-card class="box-card" shadow="hover" style="background-color: white">
            <template #header>
              <div class="card-header">
                <span style="font-size: x-large;font-weight: lighter">特殊行李</span>
              </div>
            </template>
            <div>
              <span>在此处查看有关携带特殊行李旅行时需要注意的事项，并了解需要支付哪些费用。</span>
            </div>
            <div class="bottom" style="padding-top: 10px">
              <el-button type="text" style="font-weight: bold;color: cornflowerblue" @click="SpecialBaggageDetailVisible=true">
                转到特殊行李
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-dialog v-model="AccompaniedBaggageDetailVisible" title="随身行李">
        <el-row>
          <span>
            我们将限定带入客舱行李的最大体积和/或重量。每位头等舱、公务舱旅客可携带两件随身物品，且每件重量不得超过 8 千克（17 磅）；每位经济舱旅客可携带一件随身物品，且重量不得超过 5 千克（11 磅）。每件随身携带物品长、宽、高三边分别不得超过 55 厘米 （22 英寸）、40 厘米（16 英寸）、20 厘米（8 英寸），保证可以放置在航空器客舱上方的封闭式行李架内，或放置在您的前排座椅下。如不能以上述方式放置，或由于超重超大的原因，或出于安全方面的考虑，则应当作为托运行李运输。
          </span>
        </el-row>
      </el-dialog>

      <el-dialog v-model="ConditionDetailVisible" title="行李托运限制">
        <el-row>
          <span style="font-size: large">1.尺寸</span>
          <span>
            普通托运行李尺寸：每件普通托运行李的长、宽、高三边之和：大于或等于 60 厘米（24英寸），小于或等于 158 厘米（62 英寸包括滑轮和把手）。每件行李大于 158 厘米（62 英寸），小于或等于 203 厘米（80 英寸包括滑轮和把手），须另行支付超限行李费用。
          </span>
          <span style="font-size: large;padding-top: 10px">2.重量</span>
          <span>
            豪华头等舱、头等舱、公务舱每件行李重量须大于等于 2 千克(4 磅)，且小于等于 32千克（70 磅）。悦享经济舱、超级经济舱、经济舱每件行李重量须大于等于 2 千克(4 磅)，且小于等于 23 千克（50 磅）。每件行李重量大于 23 千克(50 磅)，且小于等于 32 千克（70磅）的，须另行支付超限行李费用。所有舱位旅客托运超过 32 千克（70 磅）的普通托运行李，均须分成两件托运行李。
          </span>
          <span style="font-size: large;padding-top: 10px">3.件数</span>
          <span>
            托运行李件数限制：每位旅客托运行李数量没有数量限制（含普通行李和特殊行李）。每位旅客如需托运客票填开免费行李额、会员权益之外的第七件及以上额外付费行李（含普通行李、特殊行李、预付费行李），须提前向国航营业部/航站申请，在航班载量许可的情况下方可允许托运。同时，国航不承诺以上行李与旅客同机达到目的地。
          </span>
        </el-row>
      </el-dialog>

      <el-dialog v-model="SpecialBaggageDetailVisible" title="特殊行李">
        <el-row>
          <span >（1）特殊行李收运须符合《旅客行李运输服务手册》相关标准，否则必须作为货物运输。</span>
          <span style="padding-top: 10px">（2）未在本标准里列明的特殊行李，可与普通托运行李合并计算，超出免费托运行李限额部分，按照普通托运行李超限额收费标准（见附件 2）收取费用。</span>
          <span style="padding-top: 10px">（3）托运行李件数限制：每位旅客托运行李数量没有最大数量限制（包括普通行李和特殊行李）。每位旅客如需托运客票填开的免费托运行李限额、会员权益之外的
        第七件及以上额外付费行李（包括普通行李和特殊行李），须提前向国航营业部/航站申请，在航班载量许可的情况下方可允许托运。同时，国航不承诺以上行李与旅
        客同机到达目的地，请各营业部/航站做好旅客沟通。</span>
          <span style="padding-top: 10px">（4）残疾、伤、病旅客在美国航线免费托运轮椅的数量没有限制，其他航线可免费托运的电动轮椅或电动代步工具或手动折叠轮椅合计不超过 2 部。除此之外，还可
        运输 1 件辅助设备，包括但不限于机械假肢及专用小型气瓶、心脏起搏器或其它植入人体的装置、便携式氧气浓缩器（POC）、持续正压呼吸机（CPAP）、其它内含锂
        电池的辅助设备等。</span>
          <span style="padding-top: 10px">（5）婴儿旅客（无论何种舱位）可免费托运一件折叠式婴儿车或摇篮或婴儿汽车安全座椅。</span>
        </el-row>
      </el-dialog>

    </div>


  </div>
</template>

<script>

import request from "@/utils/request";

export default {
  name: 'HomeView',
  components: {},
  data(){
    return{
      start:'中国境内（除港澳台）',
      destination:'中国境内（除港澳台）',
      Locations:[
        {label:'中国境内（除港澳台）'},
        {label:'夏威夷'},
        {label:'美洲（除美国、加拿大外）'},
        {label:'加勒比海地区'},
        {label:'夏威夷'},
        {label:'欧洲'},
        {label:'非洲'},
        {label:'中东'},
        {label:'亚洲（除日本、巴基斯坦、新加坡、哈萨克斯坦）'},
        {label:'日本'},
        {label:'巴基斯坦'},
        {label:'新加坡'},
        {label:'哈萨克斯坦'},
        {label:'西南太平洋'},
        {label:'其他'}
      ],
      price:0,
      ticket:'成人/儿童客票',
      TicketTypes:[
        {label:'成人/儿童客票'},
        {label:'婴儿客票'}
      ],
      cabin:'经济舱',
      CabinTypes:[
        {label:'头等舱'},
        {label:'公务舱'},
        {label:'悦享经济舱、超级经济舱'},
        {label:'经济舱'}
      ],
      membership:'无',
      MembershipTypes:[
        {label:'凤凰知音终身白金卡、白金卡'},
        {label:'凤凰知音金卡、银卡'},
        {label:'星空联盟金卡'},
        {label: '无'}
      ],
      BaggageTable:[],
      BaggageIndex:0,
      SpecialBaggageTable:[],
      SpecialBaggageTypes:[
        {label:'电动轮椅'},
        {label:'电动代步工具'},
        {label:'手动折叠轮椅'},
        {label:'机械假肢及专用小型气瓶'},
        {label:'心脏起搏器或其它植入人体的装置'},
        {label:'便携式氧气浓缩器（POC）'},
        {label:'持续正压呼吸机（CPAP）'},
        {label:'其它内含锂电池的辅助设备'},
        {label:'折叠式婴儿车或摇篮或婴儿汽车安全座椅'},
        {label:'服务犬（包括：导盲犬、助听犬、辅助犬等）'},
        {label:'高尔夫球包'},
        {label:'保龄球'},
        {label:'滑翔伞/降落伞'},
        {label:'滑雪/滑水用具（不包括雪橇/水撬）'},
        {label:'轮滑/滑板用具'},
        {label:'潜水用具'},
        {label:'射箭用具'},
        {label:'曲棍球用具'},
        {label:'冰球用具'},
        {label:'网球用具'},
        {label:'登山用具'},
        {label:'自行车'},
        {label:'皮划艇/独木舟'},
        {label:'悬挂式滑翔运动用具'},
        {label:'雪橇/水撬'},
        {label:'冲浪板'},
        {label:'风帆冲浪用具'},
        {label:'橡皮艇或船'},
        {label:'撑杆'},
        {label:'标枪'},
        {label:'单独包装的划船用具或浆'},
        {label:'骑马用具'},
        {label:'睡袋'},
        {label:'背包'},
        {label:'野营用具'},
        {label:'渔具'},
        {label:'乐器'},
        {label:'辅助设备（非残疾、伤、病旅客托运）'},
        {label:'可折叠婴儿床'},
        {label:'小型电器或仪器'},
        {label:'媒体设备'},
        {label:'可作为行李运输的枪支'},
        {label:'可作为行李运输的子弹'},
        {label:'小动物（仅限家庭驯养的猫、狗）'},
      ],
      SpecialBaggageIndex:0,
      result:'0',
      AccompaniedBaggageDetailVisible:false,
      ConditionDetailVisible:false,
      SpecialBaggageDetailVisible:false
    }
  },
  created() {
    request.post("/calculator/load")
  },
  methods:{
    AddBaggage(){
      var tuple={
        size:'60',
        weight: '2',
        edit:true,
        index:0
      }
      this.BaggageTable.unshift(tuple)
    },
    SaveBaggage(row){
      row.index=this.BaggageIndex
      request.post("/calculator",row).then(res=>{
        if(res.code === '0'){
          row.edit=false
          this.BaggageIndex=this.BaggageIndex+1
        }
        else{
          this.$message({
            type:"error",
            message:"保存失败"
          })
        }
      })
    },
    DeleteBaggage(row){
      request.delete("/calculator/"+row.index).then(res=>{
        if(res.code==='0'){
          this.BaggageTable.splice(row,1)
        }
        else{
          this.$message({
            type:"error",
            message:"删除失败"
          })
        }
      })
    },
    AddSpecialBaggage(){
      var tuple={
        type:'电动轮椅',
        size:'60',
        weight:'2',
        edit:true,
        index:0
      }
      this.SpecialBaggageTable.unshift(tuple)
    },
    SaveSpecialBaggage(row){
      row.index=this.SpecialBaggageIndex
      request.post("/calculator/special",row).then(res=>{
        if(res.code==='0'){
          this.SpecialBaggageIndex=this.SpecialBaggageIndex+1
          row.edit=false
        }
        else{
          this.$message({
            type:"error",
            message:"保存失败"
          })
        }
      })
    },
    DeleteSpecialBaggage(row){
      request.delete("/calculator/special/"+row.index).then(res=>{
        if(res.code==='0'){
          this.SpecialBaggageTable.splice(row,1)
        }
        else{
          this.$message({
            type:"error",
            message:"删除失败"
          })
        }
      })
    },
    Calculate(){
      request.get("/calculator",{
        params:{
          start:this.start,
          destination:this.destination,
          price:this.price,
          ticket:this.ticket,
          cabin:this.cabin,
          membership:this.membership,
        }
      }).then(res=>{
        if(res.code==='0'){
          this.result=res.data
        }
        else{
          this.$message({
            type:"error",
            message:res.msg
          })
        }
      })
    }
  }
}
</script>


