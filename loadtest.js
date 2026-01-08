import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  vus: 500,
  duration: '20s',
};

export default function () {
  const res = http.get('http://localhost:30080/api/health')
  check(res, { 'status was 200': (r) => r.status == 200 });
  sleep(1);
}