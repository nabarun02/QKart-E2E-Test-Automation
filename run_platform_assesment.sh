# CRIO_SOLUTION_AND_STUB_ONLY_START_MODULE_DEBUGGING
python3 assesment/remoteAssesment.py "0.0.0.0" assesment/AIS_MO_01.json
# CRIO_SOLUTION_AND_STUB_ONLY_END_MODULE_DEBUGGING
# CRIO_SOLUTION_AND_STUB_ONLY_START_MODULE_TEST_AUTOMATION
python3 assesment/remoteAssesment.py "0.0.0.0" assesment/AIS_MO_01.json
# CRIO_SOLUTION_AND_STUB_ONLY_END_MODULE_TEST_AUTOMATION
# CRIO_SOLUTION_AND_STUB_ONLY_START_MODULE_SYNCHRONISATION
python3 assesment/remoteAssesment.py "0.0.0.0" assesment/AIS_MO_01.json
# CRIO_SOLUTION_AND_STUB_ONLY_END_MODULE_SYNCHRONISATION
# CRIO_SOLUTION_AND_STUB_ONLY_START_MODULE_XPATH
python3 assesment/remoteAssesment.py "0.0.0.0" assesment/AIS_MO_01.json
# CRIO_SOLUTION_AND_STUB_ONLY_END_MODULE_XPATH
# CRIO_SOLUTION_AND_STUB_START_MODULE_TESTNG
cp /tmp/external_build/reports/tests/test/testng-results.xml test-results.xml
pip3 install -r assesment/xmlAssessment/requirements.txt
python3 assesment/xmlAssessment/xmlAssessment.py assesment/xmlAssessment/Assessment_Instruction.json test-results.xml
# CRIO_SOLUTION_AND_STUB_END_MODULE_TESTNG
