import { DEFAULT_LAYOUT } from '../base';
import { AppRouteRecordRaw } from '../types';

const PREDICTION: AppRouteRecordRaw = {
  path: '/prediction',
  name: 'prediction',
  component: () => import('@/views/prediction/index.vue'),
  meta: {
    locale: 'menu.prediction',
    requiresAuth: true,
    icon: 'icon-check-circle',
    order: 2,
    roles: ['*'],
  },
}

export default PREDICTION;