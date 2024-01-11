import axios from 'axios';
import type { TableData } from "@arco-design/web-vue/es/table/interface";


export interface HomeCount {
  todayCount: number;
  flyingCount: number;
  lateCount: number;
  increase: number;
}

export interface chartData {
  x: string;
  y: number;
}

export interface PopularRecord {
  key: number;
  clickNumber: string;
  title: string;
  increases: number;
}

export interface pieChart {
  total: string;
  late: string;
  onTime: string;
  before: string;
}

export function getDayChart(days: number) {
  return axios.get<chartData[]>(`/api/flight/getDayChart?days=${days}`);
}

export function getHomeCount() {
  return axios.get<HomeCount>('/api/flight/getCount');
}

export function getRanking(params: { type: string }) {
  return axios.get<TableData[]>(`/api/flight/getRanking`, { params });
}

export function getPieChart() {
  return axios.get<pieChart>('/api/flight/getPieChart');
}