1. Public WiFi Info API
- 이 프로젝트는 서울특별시에서 제공하는 공공 와이파이 정보를 API로 제공합니다. 사용자는 위도와 경도를 입력하면 해당 지점에서 가까운 공공 와이파이 정보를 반환받을 수 있습니다.

2. Getting Started: 이 프로젝트를 사용하기 위해서는 다음과 같은 도구가 필요합니다.
- Java 8 이상
- Maven
- SQLite
- 프로젝트를 다운로드하고 빌드하기 위해서는 다음과 같은 명령어를 실행합니다.
git clone https://github.com/[user]/public-wifi-info-api.git
cd public-wifi-info-api
mvn package
위 명령어를 실행하면 target 폴더 안에 public-wifi-info-api.war 파일이 생성됩니다. 이 파일을 웹 서버에 배포하면 API를 사용할 수 있습니다.

2. Usage: API를 사용하려면 위도와 경도를 입력해야 합니다. 다음은 예시 URL입니다.
http://localhost:8080/public-wifi-info-api/selectApiServlet?LAT=37.566536&LNT=126.977966
위 URL을 실행하면 해당 지점에서 가까운 공공 와이파이 정보가 JSON 형태로 반환됩니다.

3. Built With
- Java
- Maven
- SQLite

4. Authors: John Doe - johndoe

5. License: This project is licensed under the MIT License - see the LICENSE file for details.
