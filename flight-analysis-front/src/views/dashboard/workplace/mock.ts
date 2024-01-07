import Mock from 'mockjs';
import qs from 'query-string';
import dayjs from 'dayjs';
import { GetParams } from '@/types/global';
import setupMock, { successResponseWrap } from '@/utils/setup-mock';

const textList = [
  {
    key: 1,
    clickNumber: '346.3w+',
    title: '江苏省',
    increases: 35,
  },
  {
    key: 2,
    clickNumber: '324.2w+',
    title: '广东省',
    increases: 22,
  },
  {
    key: 3,
    clickNumber: '318.9w+',
    title: '浙江省',
    increases: 9,
  },
  {
    key: 4,
    clickNumber: '257.9w+',
    title: '福建省',
    increases: 17,
  },
  {
    key: 5,
    clickNumber: '124.2w+',
    title: '甘肃省',
    increases: 37,
  },
];
const imageList = [
  {
    key: 1,
    clickNumber: '15.3w+',
    title: '兰州市',
    increases: 15,
  },
  {
    key: 2,
    clickNumber: '12.2w+',
    title: '上海市',
    increases: 26,
  },
  {
    key: 3,
    clickNumber: '18.9w+',
    title: '北京市',
    increases: 9,
  },
  {
    key: 4,
    clickNumber: '7.9w+',
    title: '广州市',
    increases: 0,
  },
  {
    key: 5,
    clickNumber: '5.2w+',
    title: '天水市',
    increases: 4,
  },
];
const videoList = [
  {
    key: 1,
    clickNumber: '367.6w+',
    title: 'FKU',
    increases: 5,
  },
  {
    key: 2,
    clickNumber: '352.2w+',
    title: 'CZU',
    increases: 17,
  },
  {
    key: 3,
    clickNumber: '348.9w+',
    title: 'CU888',
    increases: 30,
  },
  {
    key: 4,
    clickNumber: '346.3w+',
    title: 'HK996',
    increases: 12,
  },
  {
    key: 5,
    clickNumber: '271.2w+',
    title: 'ORZ886',
    increases: 2,
  },
];
setupMock({
  setup() {
    Mock.mock(new RegExp('/api/content-data'), () => {
      const presetData = [58, 81, 53, 90, 64, 88, 49, 79];
      const getLineData = () => {
        const count = 8;
        return new Array(count).fill(0).map((el, idx) => ({
          x: dayjs()
            .day(idx - 2)
            .format('YYYY-MM-DD'),
          y: presetData[idx],
        }));
      };
      return successResponseWrap([...getLineData()]);
    });
    Mock.mock(new RegExp('/api/popular/list'), (params: GetParams) => {
      const { type = 'text' } = qs.parseUrl(params.url).query;
      if (type === 'image') {
        return successResponseWrap([...videoList]);
      }
      if (type === 'video') {
        return successResponseWrap([...imageList]);
      }
      return successResponseWrap([...textList]);
    });
  },
});
