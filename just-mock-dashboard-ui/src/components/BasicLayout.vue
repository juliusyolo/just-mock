<!--
 * @Author: Julius Wong
 * @Date: 2023/3/24 11:50
 * @Description: BasicLayout页面
 * @Version: 1.0.0
-->
<template>
  <div class="basic-layout">
    <a-layout style="height: 100%;">
      <a-layout-header>
        <div class="header-title">
          <div class="header-logo">
            <a href="/">
              <svg width="155" height="32">
                <defs>
                  <linearGradient id="grad1" x1="0%" y1="0%" x2="100%" y2="0%">
                    <stop offset="0%" style="stop-color: red; stop-opacity: 1" />
                    <stop offset="100%" style="stop-color: blue; stop-opacity: 1" />
                  </linearGradient>
                </defs>
                <text font-size="32" x="0" y="30" fill="url(#grad1)">Just Mock</text>
              </svg>
            </a>
          </div>
          <div class="header-slot">
            <slot name="header"></slot>
          </div>
        </div>
      </a-layout-header>
      <a-divider style="margin: 0 0 10px 0"/>
      <a-layout-content>
        <slot name="content"></slot>
      </a-layout-content>
      <a-divider style="margin: 0"/>
      <a-layout-footer>
        <div class="footer-remark">
          <span>
            <svg width="267" height="32">
            <defs>
              <linearGradient id="grad1" x1="0%" y1="0%" x2="100%" y2="0%">
                <stop offset="0%" style="stop-color: red; stop-opacity: 1" />
                <stop offset="100%" style="stop-color: blue; stop-opacity: 1" />
              </linearGradient>
            </defs>
            <text x="0" y="30" fill="url(#grad1)">非侵入式API Mock @Author sdefaa</text>
          </svg>
          </span>
        </div>
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<script>
export default {
  name: 'BasicLayout'
}
</script>

<style lang="less" scoped>
.basic-layout {
  margin: 0 50px 0 50px;
  height: 100vh;
}

.basic-layout :deep(.arco-layout-header),
.basic-layout :deep(.arco-layout-footer),
.basic-layout :deep(.arco-layout-content) {
  display: flex;
  flex-direction: column;
  font-size: 16px;
  font-stretch: condensed;
}

.basic-layout :deep(.arco-layout-header),
.basic-layout :deep(.arco-layout-footer) {
  height: 64px;
  color: #000000;
  text-align: center;
  justify-content: center;
}
.header-title{
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  .header-logo{
    display: flex;
    flex-direction: column;
    justify-content: center;
    background: linear-gradient(to right,red,blue) no-repeat right bottom;
    background-size: 0 2px;
    transition: background-size 1300ms;
  }
  .header-logo:hover{
    background-position-x: left;
    background-size: 100% 2px;
  }
  .header-slot{
    display: flex;
    justify-content: center;
  }
}
.footer-remark span {
  background: linear-gradient(to right,red,blue) no-repeat right bottom;
  background-size: 0 2px;
  transition: background-size 1300ms;
}

.footer-remark span:hover {
  background-position-x: left;
  background-size: 100% 2px;
}

.basic-layout :deep(.arco-layout-content) {
  height: calc(100vh - 100px);
}
</style>
