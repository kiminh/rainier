package com.stripe.rainier.core

import com.stripe.rainier.compute._
import com.stripe.rainier.sampler._

trait SBCModel[T] {
  def sbc: SBC[T]
  val sampler: Sampler = HMC(1)
  val warmupIterations: Int = 10000
  val syntheticSamples: Int = 1000
  val nSamples: Int = 10
  def main(args: Array[String]): Unit = {
    implicit val rng: RNG = ScalaRNG(1528673302081L)
    sbc.animate(sampler, warmupIterations, syntheticSamples)
    println(s"\nnew goldset:")
    println(s"$samples")
    println(s"\ngoldset true value: $trueValue")
    println(
      s"If this run looks good, please update the goldset in your SBCModel")
  }
  val (samples, trueValue) = {
    implicit val rng: RNG = ScalaRNG(1528673302081L)
    val (values, trueValue) = sbc.synthesize(syntheticSamples)
    val (model, real) = sbc.fit(values)
    val samples =
      model.sample(sampler, warmupIterations, goldset.size).predict(real)
    (samples, trueValue)
  }

  def goldset: List[Double]
  val description: String
}

/** Continuous **/
object SBCUniformNormal extends SBCModel[Double] {
  def sbc = SBC(Uniform(0, 1))((x: Real) => Normal(x, 1))
  def goldset =
    List(0.41914111278901073, 0.31190601243335375, 0.4414153588703383,
      0.30551583412175415, 0.45537572113135866, 0.2936314181334304,
      0.45732667367413515, 0.28185071982333854, 0.28185071982333854,
      0.4354817591499069, 0.30938719205871956, 0.43370598496629986,
      0.3206956019493641, 0.43450075040606095, 0.32002972449565936,
      0.4022048526981999, 0.3417915358913152, 0.4062835713422497,
      0.34093681388323493, 0.4063442064614046, 0.3519140908805239,
      0.36453452463578734, 0.38086202883989334, 0.36469672157528765,
      0.37804661821651975, 0.3643788652876624, 0.38112040254148594,
      0.36323489390339186, 0.3821616909328029, 0.37852207666831156)
  val description = "Normal(x, 1) with Uniform(0, 1) prior"
}

object SBCLogNormal extends SBCModel[Double] {
  def sbc =
    SBC(LogNormal(0, 1))((x: Real) => LogNormal(x, x))
  def goldset =
    List(0.1320352047376991, 0.1278465305184665, 0.13045570289226482,
      0.12809334825262858, 0.1302285851887811, 0.12971488966061975,
      0.12858030863741665, 0.12974670510210187, 0.12864963330710052,
      0.13046537194900923, 0.12880773413827598, 0.12985465134247504,
      0.12844225339631635, 0.1313974020144121, 0.12744884255033428,
      0.1311196525734243, 0.12853622813696675, 0.12853622813696675,
      0.12977895561929287, 0.12907650766771345, 0.12983463336804282,
      0.12873418321929223, 0.12873418321929223, 0.13013268941844386,
      0.12923546640482494, 0.12958663528945488, 0.12958663528945488,
      0.1300053138358873, 0.12979024604560313, 0.12983475193607927)
  val description = "LogNormal(x, x) with LogNormal(0, 1) prior"
}

object SBCExponential extends SBCModel[Double] {
  def sbc =
    SBC(LogNormal(0, 1))((x: Real) => Exponential(x))
  def goldset =
    List(0.13478073299215504, 0.13897299818091668, 0.13468017123004908,
      0.1371532463503388, 0.13570231585735223, 0.12982110935685165,
      0.14390188813095528, 0.1303132626587145, 0.14269261389643037,
      0.13055467913521435, 0.14361307582703323, 0.13064616122621311,
      0.14064740997408012, 0.13293854606205402, 0.13293854606205402,
      0.1410400178886474, 0.13129643761313015, 0.14225691243971614,
      0.13123261688840104, 0.14286746128835248, 0.13016511066858508)
  val description = "Exponential(x) with LogNormal(0, 1) prior"
}

object SBCLaplace extends SBCModel[Double] {
  def sbc = SBC(LogNormal(0, 1))((x: Real) => Laplace(x, x))
  def goldset =
    List(0.13096444246017694, 0.1277918547414191, 0.13592294836672153,
      0.12565776451832436, 0.12565776451832436, 0.12565776451832436,
      0.13547548937902967, 0.13547548937902967, 0.12514584153518912,
      0.13364559014262625, 0.13364559014262625, 0.12769968655245909,
      0.13707261172307164, 0.12384567913734344, 0.13711350202381825,
      0.12682084744877486, 0.13323487453864688, 0.12677494620467905,
      0.13780076120266968, 0.12383245152074827, 0.13047720337074156)
  val description = "Laplace(x, x) with LogNormal(0, 1) prior"
}

object SBCGamma extends SBCModel[Double] {
  def sbc = SBC(LogNormal(0, 1))((x: Real) => Gamma(x, x))
  def goldset =
    List(0.12560178500469435, 0.1291772155289914, 0.12425228160207187,
      0.13060492117729655, 0.1238291414928327, 0.1295515446430592,
      0.1258050674360784, 0.12941082660929687, 0.1254284198459734,
      0.1286912077074183, 0.12567114238332164, 0.12896174252056614,
      0.12623461180477843, 0.1286287180956177, 0.12664314543824665,
      0.1284802407896936, 0.12654035440795755, 0.12667593234330557,
      0.12730282914479665, 0.12770816383893768, 0.1275102004765001)
  val description = "Gamma(x, x) with LogNormal(0, 1) prior"
}

/** Discrete **/
object SBCBernoulli extends SBCModel[Long] {
  def sbc =
    SBC(Uniform(0, 1))((x: Real) => Bernoulli(x))
  def goldset =
    List(0.3717408311126508, 0.3865579546493665, 0.3605987423866729,
      0.38767871113770336, 0.35470510388203624, 0.39420956481816916,
      0.39420956481816916, 0.35513855463445787, 0.37942804808657626,
      0.37942804808657626, 0.372014334585186, 0.39252999787991055,
      0.3517552444902206, 0.3517552444902206, 0.3517552444902206,
      0.38334315511668055, 0.38334315511668055, 0.3783341628260086,
      0.3654395848027485, 0.3586597336876104, 0.3968604348971254,
      0.3591944922152505, 0.3591944922152505, 0.3872505846745316,
      0.35704535693758077, 0.38829814028361664, 0.35525530505411895,
      0.38173624418624436, 0.3771013855558075, 0.3731901684779421)
  val description = "Bernoulli(x) with Uniform(0, 1) prior"
}

object SBCBinomial extends SBCModel[Long] {
  def sbc =
    SBC(Uniform(0, 1))((x: Real) => Binomial(x, 10))
  def goldset =
    List(0.4535065256280634, 0.4626187458545526, 0.4626187458545526,
      0.4511858944937367, 0.4562103990243661, 0.4562103990243661,
      0.45053233902438183, 0.45053233902438183, 0.45774584251133293,
      0.45774584251133293, 0.45206304454153684, 0.45206304454153684,
      0.45347567314971393, 0.4642348892017204, 0.4494125264409434,
      0.4440650639105339, 0.4593119144263747, 0.4593119144263747,
      0.4593119144263747, 0.4593119144263747, 0.456473316354661,
      0.456473316354661, 0.456473316354661, 0.456473316354661,
      0.4714549587182768, 0.4473437842515287, 0.4473437842515287,
      0.44781510594768753, 0.4627226838014988, 0.4627226838014988)

  val description = "Binomial(x, 10) with Uniform(0, 1) prior"
}

object SBCGeometric extends SBCModel[Long] {
  def sbc =
    SBC(Uniform(0, 1))((x: Real) => Geometric(x))
  def goldset =
    List(0.39883333471673815, 0.3826532340713303, 0.39161198399952574,
      0.384846161100462, 0.387005040552758, 0.38962811641012607,
      0.38962811641012607, 0.3855918781516388, 0.384181199416488,
      0.3826403293833026, 0.3826403293833026, 0.40112829426423696,
      0.3725233316941274, 0.3725233316941274, 0.3725233316941274,
      0.397023093995382, 0.397023093995382, 0.3855616254830426,
      0.3876218431102574, 0.3769253725684433, 0.40253160054769627,
      0.3768671034017463, 0.3768671034017463, 0.39808675173077723,
      0.3756725795523494, 0.3987655610505919, 0.37461225881412163,
      0.39579207436825403, 0.38537512278584746, 0.3911425680867883)
  val description = "Geometric(x) with Uniform(0, 1) prior"
}

object SBCGeometricZeroInflated extends SBCModel[Long] {
  def sbc =
    SBC(Uniform(0, 1))((x: Real) => Geometric(.3).zeroInflated(x))
  def goldset =
    List(0.3682741265983013, 0.3852529612682177, 0.3604238333388064,
      0.386596856795002, 0.3555033555350904, 0.39217253775032623,
      0.39217253775032623, 0.3550136947273093, 0.38182164324962603,
      0.38182164324962603, 0.36722421685006434, 0.3902898312064711,
      0.3531099114009195, 0.3942443064061138, 0.35565699293349323,
      0.38180128466000113, 0.38180128466000113, 0.37379355799257075,
      0.3692587358496806, 0.3588953235783851, 0.3931958168156864,
      0.3585549007172857, 0.39441682029588876, 0.353399981549561,
      0.38860439210841013, 0.35795264331182514, 0.38336146622540296,
      0.35691143054863744, 0.35691143054863744, 0.39069497264393727)
  val description = "Geometric(.3).zeroInflated(x) with Uniform(0, 1) prior"
}

object SBCNegativeBinomial extends SBCModel[Long] {
  def sbc =
    SBC(Uniform(0, 1))((x: Real) => NegativeBinomial(x, 10))
  def goldset =
    List(0.3842308100146834, 0.3831571422487335, 0.3819285721702851,
      0.38500689040712294, 0.38231656584391344, 0.38576141387852825,
      0.3806340420389173, 0.3856145979344356, 0.3831579621776449,
      0.3823369170300272, 0.38479698346169516, 0.3829995406491587,
      0.3825052612896004, 0.3866649433486752, 0.3802498792654533,
      0.38688289857262864, 0.3799947559174162, 0.3799947559174162,
      0.3858501161323558, 0.38059315555220735, 0.3865673200655818,
      0.3822151996834746, 0.3861711967960082, 0.38267379461607354,
      0.3846959985708584, 0.38278728435248494, 0.3837507091994167,
      0.3833984266674737, 0.38369579839376833, 0.37979951449702504)
  val description = "NegativeBinomial(x, 10) with Uniform(0, 1) prior"
}

object SBCBinomialPoissonApproximation extends SBCModel[Long] {
  def sbc =
    SBC(Uniform(0, 0.04))((x: Real) => Binomial(x, 200))
  def goldset =
    List(0.015088044867717167, 0.015139310410134868, 0.01510517009035067,
      0.01510665416274154, 0.015080615180726842, 0.015111162160004719,
      0.015111162160004719, 0.015073463905386757, 0.015116693168303188,
      0.01508608113578175, 0.01509688515262847, 0.015085878156589327,
      0.015124654968604927, 0.015094240665621138, 0.01518078580337067,
      0.015026637309531715, 0.0151594238850739, 0.01503031800274695,
      0.015163656560208318, 0.015019035418697393, 0.015175239020394615,
      0.015007932503080539, 0.015176969421428049, 0.015061150589387203,
      0.015166178775997892, 0.015052681358778401, 0.015169802322628797,
      0.015031312214831886, 0.015195099914932196, 0.014999658660327887)
  val description =
    "Poisson approximation to Binomial: Binomial(x, 200) with Uniform(0, 0.04) prior"
}

object SBCBinomialNormalApproximation extends SBCModel[Long] {
  def sbc =
    SBC(Uniform(0.4, 0.6))((x: Real) => Binomial(x, 300))
  def goldset =
    List(0.3849093699203164, 0.3822629591199987, 0.38374303903059914,
      0.3832324864849272, 0.38389219985327067, 0.3835866724851299,
      0.3830818707173923, 0.3835660923949862, 0.3841609845229018,
      0.3820783526940713, 0.3849827665275495, 0.38236510331364865,
      0.3838715727269887, 0.3840183371678221, 0.3828960655858176,
      0.3841785878664216, 0.3827156431825414, 0.38488715647596444,
      0.38157319616823965, 0.38511364643858215, 0.3819850513881394,
      0.3857373545069211, 0.3818792213826954, 0.38587080685826664,
      0.3813170524170525, 0.38588405883758004, 0.3809126103183984,
      0.38614580147352096, 0.38091354056091625, 0.3841577712144981)
  val description =
    "Normal approximation to Binomial: Binomial(x, 200) with Uniform(0.4, 0.6) prior"

}

object SBCLargePoisson extends SBCModel[Long] {
  def sbc =
    SBC(Uniform(0.8, 1))((x: Real) => Poisson(x * 1000))
  def goldset =
    List(0.8768262971603804, 0.8763748363172041, 0.8759734194840819,
      0.8764413459326335, 0.8764413459326335, 0.8758330933050529,
      0.876521774245188, 0.8754054700567884, 0.8772581578344858,
      0.8758797697701737, 0.8768100288969463, 0.8761857079880709,
      0.8763262053361434, 0.8758323114782022, 0.8762514697680838,
      0.8765975090329423, 0.8759692740351608, 0.8770904829004863,
      0.8756042692097238, 0.8773662254071924, 0.8753112057088628,
      0.8765402860449693, 0.8765816876375552, 0.8765416815649817,
      0.8761170304373757, 0.876971307073608, 0.8753959917208448,
      0.8771938779234312, 0.8750844087770844, 0.8776905156029877)

  val description =
    "Poisson(x*1000) with Uniform(0.8, 1) prior"
}
