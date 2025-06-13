package com.inlacou.inkkotlincommons.svg

import org.junit.jupiter.api.Assumptions
import org.opentest4j.AssertionFailedError
import kotlin.math.roundToInt
import com.inlacou.inkkotlincommons.svg.PathManipulator.toAbsolute
import com.inlacou.inkkotlincommons.svg.PathManipulator.tokenize
import com.inlacou.inkkotlincommons.svg.PathManipulator.toPathString
import com.inlacou.inkkotlincommons.svg.PathManipulator.getDrawnCoordinates
import com.inlacou.inkkotlincommons.svg.SvgPathManipulationMathUtils.calculateReflectionPoint
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class NormalizePathTests {

    companion object {
        class TestCase(
            val initial: String,
            val expected: String? = null,
            val name: String = "$initial to $expected"
        ) {
            override fun toString(): String = name
        }

        @JvmStatic fun doubles(): List<Double> = listOf(
            12000000000000000000.0,
            1200000000000000000.0,
            120000000000000000.0,
            12000000000000000.0,
            1200000000000000.0,
            120000000000000.0,
            12000000000000.0,
            1200000000000.0,
            120000000000.0,
            12000000000.0,
            1200000000.0,
            1200000000.0,
            120000000.0,
            12000000.0,
            1200000.0,
            120000.0,
            12000.0,
            1200.0,
            120.0,
            12.0,
            1.2,
            0.12,
            0.012,
            0.0012,
            0.00012,
            0.000012,
            0.0000012,
            0.00000012,
            0.000000012,
            0.0000000012,
            0.00000000012,
            0.000000000012,
            0.000000000001,
            0.0000000000001,
            0.00000000000001,
            0.000000000000001,
            0.0000000000000001,
            0.00000000000000001,
            0.000000000000000001,
            0.0000000000000000001,
            0.00000000000000000001,
            0.000000000000000000001,
            0.0000000000000000000001,
            0.00000000000000000000001,
            0.000000000000000000000001,
            0.0000000000000000000000001,
            0.00000000000000000000000001,
            0.000000000000000000000000001,
            0.0000000000000000000000000001,
            0.00000000000000000000000000001,
            0.000000000000000000000000000001,
            0.0000000000000000000000000000001,
            0.00000000000000000000000000000001,
            0.000000000000000000000000000000001,
            0.0000000000000000000000000000000001,
            0.5510517,
            0.5711980,
            0.1103968,
            0.0606907,
            0.5510517,
            0.31623157864917184,
            45.0,
            34457457.0
        )

        @JvmStatic fun individualTestFiles(): List<TestCase> = listOf(
            TestCase(initial = "M1.5 1L2 2L1 2 z", expected = "M0.5 0L1 1L0 1z"),
            TestCase(initial = "M2 1 L3 3 L1 3 z ", expected = "M0.5 0L1 1L0 1z"),
            TestCase(name = "box (relatives)", initial = "M 0 0 h 1 v 1 h -1 Z", expected = "M 0 0 H 1 V 1 H 0 Z"),
            TestCase(initial = "M 0.5, 0 l 0.5 1 l -1 0 Z", expected = "M0.5 0L1 1L0 1z"),
            TestCase(initial = "M0.5 0L1 1L0 1 z"),
            TestCase(name = "Gear", initial = "M0.53818 0.02611l0.03073 0.07846c0.00228 0.00583 0.0073 0.0101 0.01338 0.01141c0.00608 0.00131 0.01241 -0.00056 0.01685 -0.00494l0.05983 -0.05912c0.02657 -0.02623 0.07138 -0.00619 0.06974 0.03122l-0.00369 0.08426c-0.00028 0.00625 0.00258 0.01219 0.00761 0.01588s0.01155 0.00458 0.01738 0.0024l0.07857 -0.02955c0.03487 -0.01311 0.06771 0.02352 0.05108 0.05703l-0.03748 0.07533c-0.00278 0.00561 -0.00255 0.01222 0.00056 0.01761c0.00311 0.00541 0.00872 0.0089 0.01491 0.00929l0.08376 0.00513c0.03717 0.00226 0.05233 0.04916 0.02357 0.07296l-0.06474 0.0536c-0.0048 0.00399 -0.0073 0.01013 -0.00664 0.01632c0.00064 0.00622 0.00436 0.01172 0.00988 0.01459l0.0744 0.03892c0.03307 0.01727 0.02793 0.06629 -0.00797 0.07628l-0.08085 0.02252c-0.006 0.00167 -0.01077 0.00625 -0.01272 0.01219c-0.00192 0.00597 -0.00075 0.01247 0.00314 0.01738l0.05225 0.06596c0.02321 0.0293 -0.00133 0.07198 -0.03817 0.06643l-0.08293 -0.01242c-0.00616 -0.00092 -0.01235 0.00131 -0.01652 0.00597c-0.00416 0.00463 -0.00572 0.01108 -0.00416 0.01713l0.02102 0.08161c0.00933 0.03627 -0.03034 0.0652 -0.06174 0.04509l-0.07074 -0.04531c-0.00525 -0.00338 -0.01183 -0.00385 -0.01749 -0.00131c-0.00569 0.00254 -0.00972 0.00778 -0.01074 0.01395l-0.01388 0.08314c-0.00614 0.03694 -0.05414 0.04718 -0.07468 0.01596l-0.04628 -0.07031c-0.00344 -0.00522 -0.00925 -0.00834 -0.01546 -0.00834s-0.01202 0.00312 -0.01546 0.00834l-0.04628 0.07031c-0.02054 0.03122 -0.06855 0.02098 -0.07468 -0.01596l-0.01388 -0.08314c-0.00103 -0.00617 -0.00505 -0.01141 -0.01074 -0.01395c-0.00569 -0.00254 -0.01227 -0.00206 -0.01752 0.00131L0.25078 0.91515c-0.03148 0.02003 -0.0711 -0.00893 -0.06177 -0.04517l0.02104 -0.08161c0.00155 -0.00605 0 -0.0125 -0.00416 -0.01716c-0.00419 -0.00463 -0.01038 -0.00686 -0.01655 -0.00594l-0.08296 0.01247c-0.03684 0.00558 -0.06138 -0.03714 -0.03817 -0.06643l0.05225 -0.06596c0.00389 -0.00488 0.00508 -0.01138 0.00316 -0.01733c-0.00189 -0.00594 -0.00664 -0.01055 -0.0126 -0.01225l-0.08085 -0.02252c-0.03604 -0.00999 -0.04114 -0.05901 -0.008 -0.07628l0.07443 -0.03892c0.00552 -0.0029 0.00922 -0.00837 0.00988 -0.01459c0.00064 -0.00622 -0.00186 -0.01236 -0.00666 -0.01632l-0.06485 -0.0536c-0.02876 -0.0238 -0.0136 -0.0707 0.0236 -0.07296l0.08373 -0.00513c0.00622 -0.00036 0.01183 -0.00385 0.01494 -0.00926s0.0033 -0.01205 0.00053 -0.01763l-0.03745 -0.07533c-0.01666 -0.03348 0.01619 -0.07014 0.05108 -0.05703l0.07857 0.02955c0.00583 0.00218 0.01235 0.00128 0.01738 -0.00237c0.00503 -0.00368 0.00788 -0.00965 0.00761 -0.0159l-0.00366 -0.08432c-0.00164 -0.03741 0.0432 -0.05745 0.06974 -0.03122l0.05983 0.05912c0.00444 0.00438 0.01077 0.00625 0.01685 0.00494c0.00611 -0.00131 0.01113 -0.00558 0.01341 -0.01141l0.03071 -0.07846C0.47549 -0.0087 0.52469 -0.0087 0.53818 0.02611z",
                expected = "M 0.5381874 0.0261054L 0.5689179 0.1045668C 0.5711980 0.1103968 0.5762181 0.1146669 0.5822982 0.1159770C 0.5883783 0.1172870 0.5947084 0.1154169 0.5991485 0.1110369L 0.6589795 0.0519158C 0.6855499 0.0256854 0.7303607 0.0457257 0.7287207 0.0831364L 0.7250306 0.1673978C 0.7247506 0.1736479 0.7276106 0.1795880 0.7326407 0.1832781S 0.7441909 0.1878582 0.7500210 0.1856781L 0.8285924 0.1561276C 0.8634630 0.1430174 0.8963036 0.1796480 0.8796733 0.2131586L 0.8421926 0.2884899C 0.8394126 0.2941000 0.8396426 0.3007101 0.8427526 0.3061002C 0.8458627 0.3115103 0.8514728 0.3150003 0.8576629 0.3153903L 0.9414243 0.3205204C 0.9785950 0.3227805 0.9937552 0.3696813 0.9649947 0.3934817L 0.9002536 0.4470826C 0.8954535 0.4510727 0.8929535 0.4572128 0.8936135 0.4634029C 0.8942535 0.4696230 0.8979736 0.4751231 0.9034937 0.4779931L 0.9778949 0.5169138C 1.0109655 0.5341841 1.0058254 0.5832050 0.9699248 0.5931951L 0.8890734 0.6157155C 0.8830733 0.6173855 0.8783032 0.6219656 0.8763532 0.6279057C 0.8744332 0.6338758 0.8756032 0.6403759 0.8794932 0.6452860L 0.9317441 0.7112472C 0.9549546 0.7405476 0.9304141 0.7832284 0.8935735 0.7776783L 0.8106421 0.7652581C 0.8044820 0.7643381 0.7982918 0.7665681 0.7941218 0.7712282C 0.7899617 0.7758583 0.7884017 0.7823084 0.7899617 0.7883585L 0.8109821 0.8699699C 0.8203122 0.9062405 0.7806416 0.9351710 0.7492410 0.9150606L 0.6784998 0.8697499C 0.6732497 0.8663698 0.6666696 0.8658998 0.6610095 0.8684398C 0.6553194 0.8709799 0.6512893 0.8762200 0.6502693 0.8823901L 0.6363891 0.9655315C 0.6302490 1.0024722 0.5822482 1.0127123 0.5617078 0.9814918L 0.5154270 0.9111806C 0.5119870 0.9059605 0.5061768 0.9028404 0.4999667 0.9028404S 0.4879465 0.9059605 0.4845065 0.9111806L 0.4382257 0.9814918C 0.4176853 1.0127123 0.3696745 1.0024721 0.3635444 0.9655315L 0.3496642 0.8823901C 0.3486341 0.8762200 0.3446141 0.8709799 0.3389240 0.8684398C 0.3332339 0.8658998 0.3266538 0.8663798 0.3214037 0.8697499L 0.2507825 0.9151607C 0.2193019 0.9351910 0.1796812 0.9062305 0.1890114 0.8699899L 0.2100518 0.7883785C 0.2116018 0.7823284 0.2100518 0.7758783 0.2058917 0.7712182C 0.2017016 0.7665881 0.1955115 0.7643581 0.1893414 0.7652781L 0.1063800 0.7777483C 0.0695394 0.7833284 0.0449989 0.7406077 0.0682093 0.7113172L 0.1204602 0.6453561C 0.1243503 0.6404760 0.1255403 0.6339759 0.1236203 0.6280258C 0.1217303 0.6220856 0.1169802 0.6174756 0.1110201 0.6157755L 0.0301687 0.5932552C -0.0058717 0.5832650 -0.0109718 0.5342441 0.0221685 0.5169738L 0.0965998 0.4780532C 0.1021199 0.4751531 0.1058200 0.4696830 0.1064800 0.4634629C 0.1071200 0.4572428 0.1046200 0.4511027 0.0998199 0.4471426L 0.0349688 0.3935417C 0.0062083 0.3697413 0.0213685 0.3228405 0.0585692 0.3205805L 0.1423006 0.3154504C 0.1485207 0.3150904 0.1541308 0.3116003 0.1572409 0.3061902S 0.1605409 0.2941400 0.1577709 0.2885599L 0.1203202 0.2132286C 0.1036599 0.1797480 0.1365105 0.1430874 0.1714011 0.1561976L 0.2499725 0.1857482C 0.2558026 0.1879282 0.2623227 0.1870282 0.2673528 0.1833781C 0.2723828 0.1796981 0.2752329 0.1737279 0.2749629 0.1674778L 0.2713028 0.0831564C 0.2696628 0.0457458 0.3145036 0.0257054 0.3410440 0.0519358L 0.4008750 0.1110569C 0.4053151 0.1154370 0.4116452 0.1173070 0.4177253 0.1159970C 0.4238354 0.1146869 0.4288555 0.1104169 0.4311356 0.1045868L 0.4618461 0.0261254C 0.4754963 -0.0087050 0.5246972 -0.0087050 0.5381874 0.0261054z"),
            TestCase(name = "with curve(c)",
                initial = "M 0.5 0.02 l 0.13 0.07 c 0.37 -0.09 0.107 0.01 0.2 0.31 z",
                expected = "M 0.0591619684210527 -0.00000000000000011102230246251565L 0.4012672315789474 0.18421052631578938 C 1.3749514421052633 -0.052631578947368536 0.6828461789473685 0.21052631578947362 0.9275830210526315 1 z"),
            TestCase(name = "with curve(c) (1)", initial = "M 0 0 l 0.13 0.07 c 0.37 -0.09 0.107 0.01 0.87 0.93 z"),
            TestCase(name = "with curve(c) (2)", initial = "M 0 0 l 0.13 0.07 c 0.87 -1.07 0.107 0.01 0.87 0.93 z", expected = "M 0.1363292 0.2726585L 0.2308835 0.3235723C 0.8636707 -0.4546828 0.3087092 0.3308458 0.8636707 0.9999999z"),
            TestCase(name = "with curve(c) (3)", initial = "M 0 0 l 0.83 0.07 c 0.87 -1.07 0.107 0.01 1.87 0.93 z", expected = "M 0 0.3842352L 0.3074073 0.4101611C 0.6296296 0.0138649 0.3470369 0.4138648 1 0.7546055z"),
            TestCase(name = "weird triangle", initial = "M 0 0 l 0.83 0.07 l 1.87 0.93 z", expected = "M 0 0.3148148L 0.3074073 0.3407407L 1 0.6851851z"),
            TestCase(name = "1x1 Cube", initial = "M0 0 L1 0 L 1 1 L 0 1 Z"),
            TestCase(name = "2x2 Cube", initial = "M0 0 L2 0 L 2 2 L 0 2 Z", expected = "M0 0 L1 0 L 1 1 L 0 1 Z"),
            TestCase(name = "1x2 Rectangle (L)", initial = "M0 0 L1 0 L 1 2 L 0 2 Z", expected = "M 0.25 0.0L 0.75 0.0L 0.75 1.0L 0.25 1.0Z"),
            TestCase(name = "1x2 Rectangle (HV)", initial = "M0 0 H1 V 2 H 0 Z", expected = "M 0.25 0.0H 0.75V 1.0H 0.25Z"),
            TestCase(name = "Triangle smaller than 1x1", initial = "M 0.5 0.1 L 0.9 0.9 L 0.1 0.9 z", expected = "M 0.5 0.0L 1.0 1.0L 0.0 1.0z"),
            TestCase(name = "Simplest curve (C)", initial = "M 0 0 C 0 1 1 1 1 0z", expected = "M 0 0.125C 0 1.125 1 1.125 1 0.125z"),
            TestCase(name = "Simple curve (C)", initial = "M 0 0C 0 2 0 2 1 1z", expected = "M 0.1981973933912472 0.0C 0.1981973933912472 1.2072104264350112 0.1981973933912472 1.2072104264350112 0.8018026066087528 0.6036052132175056 z"),
            TestCase(name = "Simplest curve (Q)", initial = "M 0 0 Q 0 2 1 1 z", expected = "M 0.12496249624962502 0.00000000000000005551115123125783Q 0.12496249624962502 1.5001500150015 0.875037503750375 0.7500750075007501 z"),
            TestCase(name = "Simplest curve (S)", initial = "M 0 0 Q 0 2 1 1 S 2 0 1 0 z", expected = "M 0.00000000000000011102230246251565 0.03846537313267495Q 0.00000000000000011102230246251565 1.4232077279701338 0.6923711774187296 0.7308365505514044 S 1.384742354837459 0.03846537313267495 0.6923711774187296 0.03846537313267495 z"),
            TestCase(name = "Simplest curve (S) (2)", initial = "M 0 0 Q 0 2 1 1 S 3 1 1 0 z"),
            TestCase(name = "Simplest arc (A)", initial = "M 0 0 A 2 1 45 1 0 1 1 Z", expected = "M 0.5510517 0.1327167A 0.6324631572983437 0.31623157864917184 45.0 1 0 0.8672832 0.4489482Z"),
            TestCase(name = "Simplest arc (A)", initial = "M 0 0 A 1 1 0 1 0 1 1 Z", expected = "M 0.4999923 0A 0.5000077107077108 0.5000077107077108 0.0 1 0 1 0.5000077Z"),
            TestCase(name = "Simplest arc (A)", initial = "M 0 0 A 1 1.5 90 1 0 1 1 Z", expected = "M 0.6663885 0.1852751A 0.33361154985063857 0.5004173247759578 90.0 1 0 1 0.5188866Z"),
            TestCase(name = "Rounded box (c)", initial = "M0.77246 0H0.22754C0.10187 0 0 0.10187 0 0.22754V0.77246C0 0.89813 0.10187 1 0.22754 1H0.77246c0.12567 0 0.22754 -0.10187 0.22754 -0.22754V0.22754C1 0.10187 0.89813 0 0.77246 0z"),
            TestCase(name = "Android arm right", initial = "M0.76976 0.39624L0.76976 0.39624c-0.02537 0 -0.04594 0.02057 -0.04594 0.04594v0.19406c0 0.02537 0.02057 0.04594 0.04594 0.04594l0 0c0.02537 0 0.04594 -0.02057 0.04594 -0.04594v-0.19406C0.8157 0.41681 0.79513 0.39624 0.76976 0.39624z"),
            TestCase(name = "Android arm left", initial = "M0.23024 0.39624L0.23024 0.39624c-0.02537 0 -0.04594 0.02057 -0.04594 0.04594v0.19406c0 0.02537 0.02057 0.04594 0.04594 0.04594l0 0c0.02537 0 0.04594 -0.02057 0.04594 -0.04594v-0.19406C0.27619 0.41681 0.25561 0.39624 0.23024 0.39624z"),
            TestCase(name = "Android body", initial = "M0.29331 0.70403c0 0.02721 0.02206 0.04927 0.04927 0.04927h0.03443v0.10283c0 0.02537 0.02057 0.04594 0.04594 0.04594c0.02537 0 0.04594 -0.02057 0.04594 -0.04594v-0.10283h0.0622v0.10283c0 0.02537 0.02057 0.04594 0.04594 0.04594s0.04594 -0.02057 0.04594 -0.04594v-0.10283h0.03441c0.02721 0 0.04927 -0.02206 0.04927 -0.04927V0.40447H0.29331V0.70403z"),
            TestCase(name = "Android head", initial = "M0.6013 0.22319l0.03294 -0.05939c0.00179 -0.00323 0.00063 -0.0073 -0.0026 -0.0091c-0.00323 -0.0018 -0.0073 -0.00063 -0.0091 0.0026l-0.03333 0.0601c-0.02701 -0.01187 -0.05726 -0.01853 -0.08921 -0.01853s-0.0622 0.00666 -0.08921 0.01853L0.37746 0.1573c-0.0018 -0.00323 -0.00586 -0.0044 -0.0091 -0.0026c-0.00323 0.00179 -0.0044 0.00586 -0.0026 0.0091l0.03294 0.05939c-0.0629 0.03251 -0.10537 0.09434 -0.10537 0.1653h0.41336c0 -0.07094 -0.04249 -0.13279 -0.10537 -0.1653L0.6013 0.22319zM0.4047 0.31287c-0.00947 0 -0.01714 -0.00767 -0.01714 -0.01714c0 -0.00947 0.00767 -0.01714 0.01714 -0.01714c0.00947 0 0.01714 0.00767 0.01714 0.01714C0.42184 0.3052 0.41417 0.31287 0.4047 0.31287zM0.59529 0.31287c-0.00947 0 -0.01714 -0.00767 -0.01714 -0.01714c0 -0.00947 0.00767 -0.01714 0.01714 -0.01714s0.01714 0.00767 0.01714 0.01714C0.61243 0.3052 0.60476 0.31287 0.59529 0.31287z"),
            TestCase(name = "Star", initial = "M0.5 0L0.61801 0.38197L1 0.38197L0.69098 0.61803L0.80902 1L0.5 0.76394L0.19098 1L0.30902 0.61803L0 0.38197L0.38195 0.38197L0.5 0z"),
            TestCase(name = "Gear", initial = "M0.53818 0.02611l0.03073 0.07846c0.00228 0.00583 0.0073 0.0101 0.01338 0.01141c0.00608 0.00131 0.01241 -0.00056 0.01685 -0.00494l0.05983 -0.05912c0.02657 -0.02623 0.07138 -0.00619 0.06974 0.03122l-0.00369 0.08426c-0.00028 0.00625 0.00258 0.01219 0.00761 0.01588s0.01155 0.00458 0.01738 0.0024l0.07857 -0.02955c0.03487 -0.01311 0.06771 0.02352 0.05108 0.05703l-0.03748 0.07533c-0.00278 0.00561 -0.00255 0.01222 0.00056 0.01761c0.00311 0.00541 0.00872 0.0089 0.01491 0.00929l0.08376 0.00513c0.03717 0.00226 0.05233 0.04916 0.02357 0.07296l-0.06474 0.0536c-0.0048 0.00399 -0.0073 0.01013 -0.00664 0.01632c0.00064 0.00622 0.00436 0.01172 0.00988 0.01459l0.0744 0.03892c0.03307 0.01727 0.02793 0.06629 -0.00797 0.07628l-0.08085 0.02252c-0.006 0.00167 -0.01077 0.00625 -0.01272 0.01219c-0.00192 0.00597 -0.00075 0.01247 0.00314 0.01738l0.05225 0.06596c0.02321 0.0293 -0.00133 0.07198 -0.03817 0.06643l-0.08293 -0.01242c-0.00616 -0.00092 -0.01235 0.00131 -0.01652 0.00597c-0.00416 0.00463 -0.00572 0.01108 -0.00416 0.01713l0.02102 0.08161c0.00933 0.03627 -0.03034 0.0652 -0.06174 0.04509l-0.07074 -0.04531c-0.00525 -0.00338 -0.01183 -0.00385 -0.01749 -0.00131c-0.00569 0.00254 -0.00972 0.00778 -0.01074 0.01395l-0.01388 0.08314c-0.00614 0.03694 -0.05414 0.04718 -0.07468 0.01596l-0.04628 -0.07031c-0.00344 -0.00522 -0.00925 -0.00834 -0.01546 -0.00834s-0.01202 0.00312 -0.01546 0.00834l-0.04628 0.07031c-0.02054 0.03122 -0.06855 0.02098 -0.07468 -0.01596l-0.01388 -0.08314c-0.00103 -0.00617 -0.00505 -0.01141 -0.01074 -0.01395c-0.00569 -0.00254 -0.01227 -0.00206 -0.01752 0.00131L0.25078 0.91515c-0.03148 0.02003 -0.0711 -0.00893 -0.06177 -0.04517l0.02104 -0.08161c0.00155 -0.00605 0 -0.0125 -0.00416 -0.01716c-0.00419 -0.00463 -0.01038 -0.00686 -0.01655 -0.00594l-0.08296 0.01247c-0.03684 0.00558 -0.06138 -0.03714 -0.03817 -0.06643l0.05225 -0.06596c0.00389 -0.00488 0.00508 -0.01138 0.00316 -0.01733c-0.00189 -0.00594 -0.00664 -0.01055 -0.0126 -0.01225l-0.08085 -0.02252c-0.03604 -0.00999 -0.04114 -0.05901 -0.008 -0.07628l0.07443 -0.03892c0.00552 -0.0029 0.00922 -0.00837 0.00988 -0.01459c0.00064 -0.00622 -0.00186 -0.01236 -0.00666 -0.01632l-0.06485 -0.0536c-0.02876 -0.0238 -0.0136 -0.0707 0.0236 -0.07296l0.08373 -0.00513c0.00622 -0.00036 0.01183 -0.00385 0.01494 -0.00926s0.0033 -0.01205 0.00053 -0.01763l-0.03745 -0.07533c-0.01666 -0.03348 0.01619 -0.07014 0.05108 -0.05703l0.07857 0.02955c0.00583 0.00218 0.01235 0.00128 0.01738 -0.00237c0.00503 -0.00368 0.00788 -0.00965 0.00761 -0.0159l-0.00366 -0.08432c-0.00164 -0.03741 0.0432 -0.05745 0.06974 -0.03122l0.05983 0.05912c0.00444 0.00438 0.01077 0.00625 0.01685 0.00494c0.00611 -0.00131 0.01113 -0.00558 0.01341 -0.01141l0.03071 -0.07846C0.47549 -0.0087 0.52469 -0.0087 0.53818 0.02611z"),
            TestCase(name = "Cloud", initial = "M0.91493 0.77696c0.14031 0.16197 -0.17351 0.37111 -0.38287 0.07053c0.1091 0.26262 -0.37291 0.10834 -0.3043 -0.26539C0.03964 0.96827 -0.05044 0.5108 0.0288 0.26908C0.10736 0.02997 0.34748 -0.14675 0.53427 0.17442c0.08233 -0.12893 0.4185 -0.13769 0.21843 0.29013C0.99438 0.09497 1.07958 0.802 0.91493 0.77696L0.91493 0.77696z"),
            TestCase(name = "Splash", initial = "M0.32792 0.47191C-0.33011 -0.02927 0.60325 -0.21714 0.54413 0.33891c0.05913 -0.55606 0.71136 -0.14366 0.17866 0.11286c0.20985 -0.04852 0.27986 0.0382 0.27714 0.07415c-0.00562 0.07536 -0.11971 0.01548 -0.28095 -0.03045c0.6194 0.49034 -0.5249 0.73705 -0.26753 0.18099c-0.12098 0.26117 -0.34588 0.37231 -0.40683 0.30366c-0.06874 -0.07759 0.02086 -0.27786 0.28748 -0.36766C-0.17068 0.78194 -0.04516 0.29418 0.32792 0.47191L0.32792 0.47191z"),
            TestCase(name = "Box", initial = "M0 0H1V1H0z"),
            TestCase(name = "Box", initial = "   M0 0H1V1H0z    "),
            TestCase(name = "Triangle inside box", initial = "M 1 1 h 1 v 1 h -1 Z M 1.2 1.2 h 0.6 l -0.3 0.6 z"),
        )
    }

    @ParameterizedTest
    @MethodSource("individualTestFiles")
    fun scale_token_structure_maintained(testCase: TestCase) {
        PathManipulator.scalePath(testCase.initial).also {
            println("----------- (initial input - scales - scaled and centered)")
            println(toAbsolute(testCase.initial).toPathString())
            println(it)
            val centered = PathManipulator.centerPath(it)
            println(centered)

            val original = tokenize(path = testCase.initial)
            val final = tokenize(centered)

            Assertions.assertEquals(original.map { it.command.letterEnum }, final.map { it.command.letterEnum })
            Assertions.assertEquals(original.map { it.coords.toList() }.flatten().size, final.map { it.coords.toList() }.flatten().size)
        }
    }

    @ParameterizedTest
    @MethodSource("individualTestFiles")
    fun scale_points_inside_1x1(testCase: TestCase) {
        println("----------- (initial input - absoluted")
        testCase.initial.also { println(it) }
            .let { toAbsolute(it) }.also { println(it.toPathString()) }
        PathManipulator.scalePath(testCase.initial).also {
            println("----------- (initial input - scaled - scaled and centered)")
            println(testCase.initial)
            println(it)
            val centered = PathManipulator.centerPath(it)
            println(centered)

            val lowThreshold = -0.0000001
            val highThreshold = 1.0000001
            assert(getDrawnCoordinates(centered)
                .also {
                    println()
                    println("----------- All sampled points: ${it.size}")
                    println(it.map { "L ${it.x!!.toStringAvoidScientificNotation()} ${it.y!!.toStringAvoidScientificNotation()}" }
                        .reduce { acc, s -> acc+s }
                        .replaceFirst("L", "M")+" Z")
                }.all { coord ->
                coord.x!! in lowThreshold..highThreshold && coord.y!! in lowThreshold..highThreshold
            })
        }
    }

    /**
     * This method could fail while code being ok for example by removing centering after scale.
     */
    @ParameterizedTest
    @MethodSource("individualTestFiles")
    fun scale_equals_expected(testCase: TestCase) {
        PathManipulator.scalePath(testCase.initial).also {
            println("----------- (initial input - initial (absolute) - scaled - scaled and centered)")
            println(testCase.initial)
            println(toAbsolute(testCase.initial).toPathString())
            println(it)
            val centered = PathManipulator.centerPath(it)
            println(centered)

            Assumptions.assumeTrue(testCase.expected!=null)
            assertListEquals(toAbsolute(testCase.expected!!), tokenize(centered))
        }
    }

    @Test fun reflectionPoint() {
        Assertions.assertEquals(Point(2.0, 2.0), Point(0.0, 0.0).calculateReflectionPoint(Point(1.0, 1.0)))
        Assertions.assertEquals(Point(-1.0, -1.0), Point(1.0, 1.0).calculateReflectionPoint(Point(0.0, 0.0)))
        Assertions.assertEquals(Point(1.0, 1.0), Point(1.0, 1.0).calculateReflectionPoint(Point(1.0, 1.0)))
        Assertions.assertEquals(Point(1.0, 3.0), Point(1.0, 1.0).calculateReflectionPoint(Point(1.0, 2.0)))
        Assertions.assertEquals(Point(5.0, 1.0), Point(1.0, 1.0).calculateReflectionPoint(Point(3.0, 1.0)))
    }

    private fun assertListEquals(first: List<FullCommand>, second: List<FullCommand>) {
        if(first.size != second.size) throw AssertionFailedError("Lists are not equal - different sizes: ${first.size} vs ${second.size}\n$first\n$second")
        val differences = mutableListOf<String>()
        first.forEachIndexed { index, t ->
            second[index].let { second ->
                if(!t.equalsWithTolerance(second)) {
                    if(t.command != second.command) differences.add("elements at index $index not equal (command): \n\t$t \n\t${second}")
                    if(!t.coords.contentEquals(second.coords)) differences.add("elements at index $index not equal (coords): \n\t$t \n\t${second}")
                }
            }
        }
        if(differences.isNotEmpty()) throw AssertionFailedError(
            "Lists are not equal - different content:\n$first\n$second\n\n" + differences.reduce { acc, s -> "$acc\n$s" }
        )
    }

    private fun FullCommand.equalsWithTolerance(other: Any?, tolerance: Double = 1000.0): Boolean {
        if (this === other) return true
        if (other !is FullCommand) return false

        if(command != other.command) return false
        if(this is ACommand) {
            val other = other as ACommand
            if(sweepFlag != other.sweepFlag) return false
            if(largeArcFlag != other.largeArcFlag) return false
            if(rotation != other.rotation) return false
        }
        coords.forEachIndexed { index, coordinate ->
            if (!coordinate.equalsWithTolerance(other.coords[index], tolerance)) return false
        }

        return true
    }

    private fun SvgPathToken.Coordinate.equalsWithTolerance(other: Any?, tolerance: Double = 1000.0): Boolean {
        if (this === other) return true
        if (other !is SvgPathToken.Coordinate) return false

        if (!x.equalsWithTolerance(other.x, tolerance)) return false
        if (!y.equalsWithTolerance(other.y, tolerance)) return false

        return true
    }

    private fun Double?.equalsWithTolerance(other: Double?, tolerance: Double = 1000.0): Boolean {
        return if(this == null && other == null) true
        else if(this == null && other != null) false
        else if(this != null && other == null) false
        else ((this!!*tolerance).roundToInt() == (other!!*tolerance).roundToInt()).also {
            if(!it) println("compared ${this*tolerance} to ${other*tolerance} UNEQUAL")
        }
    }

    @ParameterizedTest
    @MethodSource("doubles")
    fun avoid_scientific_notation_custom(input: Double) {
        println("input: $input")
        input.toStringAvoidScientificNotation().let {
            println("avoid: $it (${it.toDouble()==input}) (${it.count { it=='0' }} 0s)")
            Assertions.assertEquals(input, it.toDouble())
            Assertions.assertFalse { it.contains("E") }
        }
    }

}