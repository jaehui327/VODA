<template>
  <v-app>
    <v-flex xs12 class="mb-3">
      <vc-calendar
        ref="calendar"
        class="custom-calendar"
        :masks="{ title: 'YYYY-MM' }"
        @update:from-page="handleCalendarChange"
      >
        <template v-slot:day-content="{ day }">
          <div class="flex flex-col z-100">
            <div class="day-header">{{ day.day }}</div>
            <div
              v-show="handleEmotionButton(day.day)"
              @click="handleDateClick(day.date)"
            >
              <img
                v-if="handleEmotionButton(day.day)"
                :src="
                  require(`@/assets/emotions/${
                    calendarData[day.day - 1].emotionImgUrl
                  }`)
                "
                alt="emotion"
              />
            </div>
          </div>
        </template>
      </vc-calendar>
    </v-flex>
  </v-app>
</template>

<script>
export default {
  data: () => ({
    userId: localStorage.getItem("userSeq"),
    year: null,
    month: null,
    date: new Date(),
    calendarData: new Array(31).fill({
      status: false,
      emotionImgUrl: "",
      calendarSeq: 0,
    }),
  }),
  created() {
    this.$store.dispatch("setShowBottomNavigation", true);
    this.$store.dispatch("setShowTopNavigation", true);

    this.month = this.date.getMonth() + 1;
    this.year = this.date.getFullYear();
    const formattedDate = `${this.year}-${
      this.month < 10 ? "0" + this.month : this.month
    }-01`;

    this.$store
      .dispatch("getCalendarInfo", {
        userSeq: this.userId,
        date: formattedDate,
      })
      .then(() => {
        const calendarData = this.$store.getters.calendarData;
        const size = Object.keys(calendarData).length;
        if (size != 0) {
          for (let i = 0; i < size; i++) {
            const numberStr = calendarData[i].date.split("-")[2];
            const number = Number(numberStr);
            this.calendarData.splice(number - 1, 1, {
              status: true,
              emotionImgUrl: calendarData[i].emotionImgUrl,
              calendarSeq: calendarData[i].calendarSeq,
            });
          }
        }
      });
    this.$store.dispatch("setShowBottomNavigation", true);
  },
  methods: {
    handleDateClick(date) {
      const year = date.getFullYear();
      const month = date.getMonth() + 1;
      const day = date.getDate();
      const formattedDate = `${year}.${month < 10 ? "0" + month : month}.${
        day < 10 ? "0" + day : day
      }`;
      this.$router.push({
        path: `/calendar/diary/${this.calendarData[day - 1].calendarSeq}`,
        query: { formattedDate },
      });
    },
    handleEmotionButton(date) {
      return this.calendarData[date - 1].status;
    },
    handleCalendarChange() {
      this.month = this.$refs.calendar.pages[0].month;
      this.calendarData.fill({
        status: false,
        emotionImgUrl: "",
        calendarSeq: 0,
      });
      this.year = this.$refs.calendar.pages[0].year;
      const formattedDate = `${this.year}-${
        this.month < 10 ? "0" + this.month : this.month
      }-01`;
      this.$store
        .dispatch("getCalendarInfo", {
          userSeq: this.userId,
          date: formattedDate,
        })
        .then(() => {
          const calendarData = this.$store.getters.calendarData;
          const size = Object.keys(calendarData).length;
          if (size != 0) {
            for (let i = 0; i < size; i++) {
              const numberStr = calendarData[i].date.split("-")[2];
              const number = Number(numberStr);
              this.calendarData.splice(number - 1, 1, {
                status: true,
                emotionImgUrl: calendarData[i].emotionImgUrl,
                calendarSeq: calendarData[i].calendarSeq,
              });
            }
          }
        });
    },
  },
};
</script>

<style scoped lang="scss">
@font-face {
  font-family: "NanumSquareNeoa";
  src: url("../../public/fonts/NanumSquareNeo-dEb.ttf");
  font-weight: 900;
}
::-webkit-scrollbar {
  width: 0px;
}
.day-label {
  font-size: 14px;
}
.day-header {
  text-align: center;
  font-size: 18px;
}
::-webkit-scrollbar-track {
  display: none;
}
//달력 테두리
::v-deep .custom-calendar.vc-container {
  //달력 내부 선
  font-family: "NanumSquareNeoa";
  margin-top: 70px;
  margin-bottom: 70px;
  //날짜별 크기

  //요일칸 색상
  --weekday-bg: #f8fafc;
  //요일 테두리 색상
  --weekday-border: 1px solid #eaeaea;
  .vc-title {
    color: beige;
    font-size: 25px;
  }
  border-radius: 10px 10px 0px 0px;
  //달력 너비
  width: 90%;
  --day-width: width/7%;
  --day-height: 12vh;
  .vc-arrow {
    color: beige;
    background: transparent;
    box-shadow: 0ch;
  }
  .vc-header {
    padding: 10px 0;
    background: linear-gradient(#97c7ff, #855cf8);
    border-radius: 10px 10px 0px 0px;
  }
  .vc-weeks {
    padding: 0;
  }
  .vc-weekday {
    background-color: var(--weekday-bg);
    border-bottom: var(--weekday-border);
    border-top: var(--weekday-border);
    padding: 10px 0;
    color: black;
    font-size: 18px;
  }
  .vc-day {
    padding: 0 5px 3px 5px;
    text-align: center;
    height: var(--day-height);
    min-width: var(--day-width);
    background-color: white;
    overflow: hidden;

    &.weekday-1,
    &.weekday-7 {
      color: red;
    }

    &:not(.on-bottom) {
      border-bottom: var(--day-border);

      &.weekday-1 {
        border-bottom: var(--day-border-highlight);
      }
    }

    &:not(.on-right) {
      border-right: var(--day-border);
    }
  }

  .vc-day-dots {
    margin-bottom: 5px;
  }
  img {
    height: var(--day-height);
    min-width: var(--day-width);
  }
}
@media only screen and (max-width: 600px) {
  ::v-deep .custom-calendar.vc-container {
    .day-header {
      font-size: 14px;
    }
    .vc-title {
      font-size: 25px;
    }
    border-radius: 10px 10px 0px 0px;
    //달력 너비
    width: 90%;
    .vc-arrow {
    }
    .vc-header {
    }
    .vc-weekday {
      padding: 5px 0;
      font-size: 15px;
    }
  }
}
</style>
