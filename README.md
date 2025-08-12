# hanaro

## 모니터링 및 로그 관리

### Spring Actuator
- **접근 URL**: http://localhost:9001/actuator
    - `/actuator/health` - 애플리케이션 상태 확인
    - `/actuator/metrics` - 메트릭 정보
    - `/actuator/loggers` - 로거 설정 확인/변경

### 스케줄러 작업
1. **주문 상태 자동 변경**
    - `결제완료 → 배송준비`: 매 5분마다 실행 (`0 */5 * * * *`)
    - `배송준비 → 배송중`: 매 15분마다 실행 (`0 */15 * * * *`)
    - `배송중 → 배송완료`: 매 시간마다 실행 (`0 0 * * * *`)

2. **통계 배치 작업**
    - 매일 23:59:59에 실행 (`59 59 23 * * *`)
    - 일일 매출 통계 생성

### 로그 확인 방법
로그 파일 위치: `./logs/`

**비즈니스 로그**
- `business_product.log`: 상품/장바구니 관련 로그
- `business_order.log`: 주문 관련 로그
- `application.log`: 일반 애플리케이션 로그
