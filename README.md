# Android BLE Sample

BLE(블루투스 저전력) 스캔 시작점을 빠르게 만들기 위한 안드로이드 보일러플레이트 프로젝트입니다.

## 포함된 내용
- Kotlin + Android ViewBinding 기반 단일 `:app` 모듈
- BLE 관련 기본 권한/피처 선언 (`BLUETOOTH_SCAN`, `BLUETOOTH_CONNECT` 등)
- 런타임 권한 요청 후 BLE 스캔을 시작하는 기본 `MainActivity`
- 스캔 상태 및 발견 디바이스 표시용 간단한 UI

## 로컬 빌드 준비
이 저장소는 PR 시스템 제약(바이너리 파일 업로드 불가) 때문에 `gradle-wrapper.jar`를 포함하지 않습니다.

로컬에서 아래 명령으로 wrapper를 생성한 뒤 실행하세요.

```bash
gradle wrapper --gradle-version 8.7 --no-validate-url
./gradlew assembleDebug
```
