Page({
  data: {
    logos: [
      "/assets/images/kvl.png", 
      "/assets/images/kee.png", 
      "/assets/images/kpt.png"
    ],
    companies: [
      "精典电子股份有限公司", 
      "上海精泰机电系统工程有限公司", 
      "Kingpoint Technology Pte. Ltd."
    ],
    addresses: [
      "上海市浦东新区新金桥路28号新金桥大厦30F,31F", 
      "上海市浦东新区新金桥路28号新金桥大厦30F,31F", 
      "No.12, Woodlands Square, #08-81, Woods Square Tower 1, Singapore 737715"
    ],
    httpses: [
      "https://www.kvl.cn",
      "https://www.kvl.cn",
      "https://kptsg.com"
    ],

    companyNumber: 2,
    name: "李磊磊磊",
    occupation: "技术部总监",
    tele: "13912345678",
    email: "abc_abc@kvl.cn",
    address: "上海市浦东新区新金桥路28号新金桥大厦30F,31F",
    https: "https://www.kvl.cn",
    companyIntro: "精典电子股份有限公司(KVL)始创于1985年，立足于中国微电子相关(半导体、平板显示、太阳能电池LED......)市场。通过整合全球微电子相关行业资源为中国本土微电子相关制造客户和立志于开拓中国市场的全球微电子相关跨国企业提供性价比最好的技术、产品和服务。"
  },
  
  onLoad: function() {
    this.setData({
      logo: this.data.logos[this.data.companyNumber],
      company: this.data.companies[this.data.companyNumber],
      address: this.data.addresses[this.data.companyNumber],
      https: this.data.httpses[this.data.companyNumber],
    })
  },
  
  handleGetNow: function() {
    console.log("跳转")
  }
});