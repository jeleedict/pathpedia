/*******************************************************************************
 * Copyright (c) 2011, 2014 Kyungpook National University and Contributors
 *
 * Contributor(s): - Hyun-Je Song
 *******************************************************************************/
package kr.ac.knu.ml.parser.surgeon;

import static org.junit.Assert.*;

import org.junit.Test;

public class SlideKeyParserTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	public static void main(String[] args) {
//		String slideKey = "(slide key: 1-3: 좌측 갑상선 (1,2: 종괴, 3: 주위실질), 4: 우측 갑상선 대표단면, A: left level III, B: left level IV, C: central LN, D: jugular LN)";		
//		String slideKey = "(slide key: 1-7: 종괴 부위 모두 포매 (1: 대표 단면, 2-4: lower pole쪽으로 serial로 포매, 5-7: upper pole쪽으로 serial로 포매), 8,9: 주변 실질 (upper pole), A: \"Rt. central node\") ";
//		String slideKey = "(slide key: 1-18: mapping(1-14: CBD 병변부위 포함하는 단면, 15-18: 담낭 대표 한줄, 19: falciform 대표단면)";
//		String slideKey = "(slide key: 1-6: medial resection margin에서부터 mappping(1: medial RM), 7-9: inferior RM 덧대어져온 부위(모두 포매), 10: lateral RM, MG1: M2.5, 2-3: M3.0, 4-5: M3.5, 6-7: M4.0, 8-9: M4.5, 10-11: M5.0, 12-13: M5.5, 14: M6.0)";
//		String slideKey = "(slide key: 1-17: mapping (1: Rt. base, 2: Lt. base, 17: Rt. apex, 18: Lt. apex), 19: 오른쪽 정낭과 정관, 20: 왼쪽 정낭과 정관, A: perivesical fat)";
//		String slideKey = "(slide key: 1-25: mapping, A: \"L/N\"이라고 표기되어 온 조직, B: \"Paraaortic LN\", C: \"Paraaortic LN #2\"";
//		String slideKey = "(slide key: 1: distal resection margin, 2: distal에서 proximal resection margin까지 연속 절개하여 전부 포매, 3:";
//		String slideKey = "(slide key: 1-4: 대장의 장막에서 점막까지 한줄, 5-8: 장막 부분에서 대장 점막까지 대표단면, (";
//		String slideKey = "(slide key: 1-4: 종축을 따라 한줄, 5-11: 종괴 부위 mapping), A: #7, B: #8L, C: 8L, D: #8M, E: #16, F:#17, G: Rt.";
		String slideKey = "(slide key: 1-5: 종축을 따라 한 줄 (1: 원위 절연, 2,3: 종괴, 4: IC valve, 5: 근위 절연), 6-8: 종괴 다른 단면들, ";
//		String slideKey = "(slide key: 1:  혈관 및 담관 대표 단면, 2: right liver, 3: left liver, 4: hilar liver, 5-6: cystic한 병변 부위 포함하는 주변 간 실질 단면, 7; 담낭, 8: falciform ligament)";
//		String slideKey = "(slide key: 1: 첫번째 결절성 병변, 2: 두번째 결절성 병변, 3: 세번째 결절성 병변, 4: 네번째 결절성 병변, 5: 다섯번째 결절성 병변, 6: 실질 내부에 연한 붉은색으로 관찰되는 부위, 7: 간우엽 실질, 8: 간좌엽 실질, 9: 담낭, 10: 따로 온 LN)";
		
		SlideKeyParser skp = new SlideKeyParser();
		System.out.println ( slideKey );
		System.out.println ( skp.slidesSplitor( slideKey ) );
	}
}
